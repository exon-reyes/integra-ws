package integra.asistencia.service.rutinas;

import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.repository.AsistenciaRepository;
import integra.asistencia.repository.EmpleadoPuestoService;
import integra.asistencia.repository.PausaModelRepository;
import integra.config.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JornadaCierreScheduler {

    private final AsistenciaRepository asistenciaRepository;
    private final PausaModelRepository pausaRepository;
    private final EmpleadoPuestoService empleadoPuestoService;
    private final EmailService emailService;

    // Formateadores para que las fechas se vean amigables en el correo
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Scheduled(cron = "00 30 23 * * ?")
    @Transactional
    public void cerrarJornadasDiurnasAbiertas() {
        log.info("=== CCA Diurno: Iniciando proceso ===");
        procesarCierreJornadas(false);
        log.info("=== CCA Diurno: Finalizado ===");
    }

    @Scheduled(cron = "00 00 10 * * ?")
    @Transactional
    public void cerrarJornadasNocturnasAbiertas() {
        log.info("=== CCA Nocturno: Iniciando proceso ===");
        procesarCierreJornadas(true);
        log.info("=== CCA Nocturno: Finalizado ===");
    }

    /**
     * Lógica unificada para cerrar jornadas.
     * @param esProcesoNocturno true si es el scheduler de las 10am (para turnos noche), false si es el de las 11pm.
     */
    private void procesarCierreJornadas(boolean esProcesoNocturno) {
        List<AsistenciaModel> jornadasAbiertas = asistenciaRepository.findAllByJornadaCerradaFalseWithEmpleado();

        if (jornadasAbiertas.isEmpty()) return;

        for (AsistenciaModel jornada : jornadasAbiertas) {
            final Integer empleadoId = jornada.getEmpleado().getId();
            final LocalDateTime inicio = jornada.getInicioJornada();

            // Determina si la jornada específica es nocturna (inició después de las 20:00)
            boolean esTurnoNoche = inicio.getHour() >= 20 || empleadoPuestoService.tienePuestoNocturno(jornada.getEmpleado().getPuesto().getId());

            // Filtro: Si el scheduler es diurno, salta las nocturnas. Si es nocturno, salta las diurnas.
            if (esProcesoNocturno != esTurnoNoche) continue;

            // Calcular hora de cierre (CCA)
            LocalDateTime ccaTime = esTurnoNoche
                    ? inicio.toLocalDate().plusDays(1).atTime(9, 59) // Cierre nocturno
                    : inicio.toLocalDate().atTime(23, 59);           // Cierre diurno

            // 1. Cerrar pausas activas si existen
            cerrarPausasActivas(empleadoId, ccaTime);

            // 2. Enviar notificación (Solo avisa falta de registro y folio)
            enviarNotificacionSinSalida(jornada);

            // 3. Cerrar jornada en DB
            jornada.setFinJornada(ccaTime);
            jornada.setJornadaCerrada(true);
            jornada.setComentario("Cierre automático por falta de registro de salida.");
            jornada.setCerradoAutomatico(true);
            asistenciaRepository.save(jornada);

            log.info("Jornada ID {} cerrada (CCA).", jornada.getId());
        }
    }

    private void cerrarPausasActivas(Integer empleadoId, LocalDateTime finTime) {
        pausaRepository.findFirstByAsistencia_Empleado_IdAndFinNullOrderByInicioDesc(empleadoId)
                .ifPresent(pausa -> {
                    pausa.setFin(finTime);
                    pausaRepository.save(pausa);
                });
    }

    /**
     * Genera y envía el correo con diseño corporativo minimalista.
     */
    private void enviarNotificacionSinSalida(AsistenciaModel jornada) {
        String nombre = jornada.getEmpleado().getNombre();
        String folio = String.valueOf(jornada.getId()); // ID como Folio
        String fechaStr = jornada.getInicioJornada().format(DATE_FMT);
        String entradaStr = jornada.getInicioJornada().format(TIME_FMT);

        // Diseño "Card" Corporativo: Sin degradados, bordes finos, colores sólidos.
        String htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <body style="margin:0; padding:0; font-family: 'Segoe UI', Arial, sans-serif; background-color: #f8fafc;">
                <div style="max-width: 500px; margin: 40px auto; background: #ffffff; border: 1px solid #e2e8f0; border-radius: 8px; overflow: hidden;">
                    
                    <div style="background-color: #1e293b; padding: 16px 24px; display: flex; align-items: center; justify-content: space-between;">
                        <span style="color: #94a3b8; font-size: 12px; text-transform: uppercase; letter-spacing: 1px;">NOTIFICACIÓN</span>
                    </div>
        
                    <div style="padding: 32px 24px;">
                        <h2 style="margin: 0 0 12px 0; color: #0f172a; font-size: 18px;">Sin registro de salida</h2>
                        <p style="color: #64748b; font-size: 14px; line-height: 1.6; margin-bottom: 24px;">
                            Hola <strong>%s</strong>, detectamos que tu jornada finalizó sin un registro de salida en el sistema.
                        </p>
        
                        <div style="background-color: #f1f5f9; border-radius: 6px; padding: 16px;">
                            <table style="width: 100%%; border-collapse: collapse;">
                                <tr>
                                    <td style="color: #64748b; font-size: 12px; padding: 4px 0;">Fecha</td>
                                    <td style="color: #0f172a; font-size: 13px; font-weight: 600; text-align: right;">%s</td>
                                </tr>
                                <tr>
                                    <td style="color: #64748b; font-size: 12px; padding: 4px 0;">Hora de Entrada</td>
                                    <td style="color: #0f172a; font-size: 13px; font-weight: 600; text-align: right;">%s</td>
                                </tr>
                            </table>
                        </div>
        
                        <p style="margin-top: 24px; font-size: 13px; color: #64748b; text-align: center;">
                            Si esto fue un error u olvido, puedes contactar a <strong>Recursos Humanos</strong> proporcionando el folio #%s.
                        </p>
                    </div>
        
                    <div style="background-color: #f8fafc; padding: 12px; text-align: center; border-top: 1px solid #e2e8f0;">
                        <p style="margin: 0; color: #cbd5e1; font-size: 11px;"> Integra <strong>| Reloj Checador</strong></p>
                    </div>
                </div>
            </body>
            </html>
            """;

        try {
            // Se envía al correo del empleado (o al default definido en tu ejemplo)
            emailService.sendHtmlEmail(
                    "exon1704@gmail.com",
                    "Aviso de Asistencia - Folio #" + folio,
                    htmlTemplate.formatted(nombre,fechaStr,entradaStr, folio )
            );
        } catch (Exception e) {
            log.error("Error enviando notificación al empleado ID {}: {}", jornada.getEmpleado().getId(), e.getMessage());
        }
    }
}