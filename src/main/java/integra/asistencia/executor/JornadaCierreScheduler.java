package integra.asistencia.executor;

import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.repository.AsistenciaRepository;
import integra.asistencia.repository.EmpleadoPuestoService;
import integra.asistencia.repository.PausaModelRepository;
import integra.config.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class JornadaCierreScheduler {

    // Formato de fecha/hora
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AsistenciaRepository asistenciaRepository;
    private final PausaModelRepository pausaRepository;
    private final EmpleadoPuestoService empleadoPuestoService;
    private final EmailService emailService;
    private final Executor jornadaExecutor;

    public JornadaCierreScheduler(
            AsistenciaRepository asistenciaRepository,
            PausaModelRepository pausaRepository,
            EmpleadoPuestoService empleadoPuestoService,
            EmailService emailService,
            @Qualifier("jornadaExecutor") Executor jornadaExecutor) {
        this.asistenciaRepository = asistenciaRepository;
        this.pausaRepository = pausaRepository;
        this.empleadoPuestoService = empleadoPuestoService;
        this.emailService = emailService;
        this.jornadaExecutor = jornadaExecutor;
    }

    // =====  SCHEDULES =====
    @Scheduled(cron = "00 40 23 * * ?")
    public void cerrarJornadasDiurnas() {
        ejecutarCierre(false);
    }

    @Scheduled(cron = "00 00 10 * * ?")
    public void cerrarJornadasNocturnas() {
        ejecutarCierre(true);
    }

    // ===== LÓGICA PRINCIPAL
    private void ejecutarCierre(boolean esProcesoNocturno) {

        List<AsistenciaModel> jornadas = asistenciaRepository.findAllByJornadaCerradaFalseWithEmpleado();

        if (jornadas.isEmpty()) return;

        jornadas.forEach(jornada ->
                jornadaExecutor.execute(() -> procesarJornada(jornada, esProcesoNocturno))
        );
    }

    @Transactional
    public void procesarJornada(AsistenciaModel jornada, boolean esProcesoNocturno) {

        try {
            LocalDateTime inicio = jornada.getInicioJornada();

            boolean esTurnoNoche =
                    inicio.getHour() >= 20 ||
                            empleadoPuestoService.tienePuestoNocturno(jornada.getEmpleado().getPuesto().getId());

            if (esProcesoNocturno != esTurnoNoche)
                return;

            LocalDateTime cierre = esTurnoNoche
                    ? inicio.toLocalDate().plusDays(1).atTime(9, 59)
                    : inicio.toLocalDate().atTime(23, 59);

            // Cerrar pausa activa si existe
            cerrarPausa(jornada.getEmpleado().getId(), cierre);

            // Enviar correo asíncrono (NO bloquea)
            enviarCorreoAsync(jornada);

            // Actualización de la jornada
            jornada.setFinJornada(cierre);
            jornada.setJornadaCerrada(true);
            jornada.setComentario("Cierre automático por falta de registro de salida.");
            jornada.setCerradoAutomatico(true);

            asistenciaRepository.save(jornada);

            log.info("Cierre OK: Jornada {}", jornada.getId());

        } catch (Exception ex) {
            log.error("Error procesando jornada {}: {}", jornada.getId(), ex.getMessage());
        }
    }

    private void cerrarPausa(Integer empleadoId, LocalDateTime cierre) {
        pausaRepository.findFirstByAsistencia_Empleado_IdAndFinNullOrderByInicioDesc(empleadoId)
                .ifPresent(p -> {
                    p.setFin(cierre);
                    pausaRepository.save(p);
                });
    }

    // -----------------------------
    //       ENVÍO DE CORREO
    // -----------------------------

    @Async("emailExecutor")
    public void enviarCorreoAsync(AsistenciaModel jornada) {

        try {
            String nombre = jornada.getEmpleado().getNombre();
            String fecha = jornada.getInicioJornada().format(DATE_FMT);
            String entrada = jornada.getInicioJornada().format(TIME_FMT);
            String folio = String.valueOf(jornada.getId());

            String html = generarHtml(nombre, fecha, entrada, folio);

            emailService.sendHtmlEmail(
                    "exon1704@gmail.com",
                    "Registro con error",
                    html
            );

        } catch (Exception e) {
            log.error("Error enviando correo jornada {}: {}", jornada.getId(), e.getMessage());
        }
    }

    // ==================================================
    // PLANTILLA HTML PARA EL ENVÍO DE NOTIFICACIÓN DE JORNADA SIN REGISTRO DE SALIDA
    // ==================================================

    private String generarHtml(String nombre, String fecha, String entrada, String folio) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0; padding:0; font-family:'Google Sans', Arial, sans-serif; background:#f5f7fa;">
                    <div style="max-width:460px; margin:32px auto; background:#ffffff; border:1px solid #d6dce5; border-radius:8px; overflow:hidden;">
                        <div style="background:#0A4A9C; padding:14px 20px;">
                            <span style="color:#ffffff; font-size:12px; font-weight:600;"> Notificación · Sin registro de salida </span>
                        </div>
                        <div style="padding:26px 22px; color:#0f172a;">
                            <h2 style="margin:0 0 10px 0; font-size:17px; font-weight:600;">¡Hola %s!</h2>
                            <p style="font-size:13px; color:#475569; line-height:1.55;">
                                Se detectó que tu jornada terminó sin un registro de salida.
                            </p>
                            <div style="background:#f0f4fa; border-radius:6px; padding:14px; margin-top:14px;">
                                <table style="width:100%%; font-size:13px;">
                                    <tr><td style="color:#64748b;">Fecha</td><td style="text-align:right; font-weight:600;">%s</td></tr>
                                    <tr><td style="color:#64748b;">Hora de entrada</td><td style="text-align:right; font-weight:600;">%s</td></tr>
                                    <tr><td style="color:#64748b;">Hora de salida</td><td style="text-align:right; font-weight:600; color:#d62839;">Sin registro</td></tr>
                                </table>
                            </div>
                            <p style="font-size:13px; color:#475569; line-height:1.55; margin-top:18px; text-align: justify;">
                                                            Tu evento ha sido notificado al equipo encargado. Folio de soporte <strong style="color:#0A4A9C;">#%s</strong>.
                            </p>
                        </div>
                        <div style="background:#f5f7fa; padding:10px; text-align:center; border-top:1px solid #e5e9f1;">
                            <p style="margin:0; font-size:11px; color:#94a3b8;">Integra · Notificación automática</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(nombre, fecha, entrada, folio);
    }
}
