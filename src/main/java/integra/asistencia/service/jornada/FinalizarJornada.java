package integra.asistencia.service.jornada;

import integra.asistencia.actions.FinalizarJornadaCommand;
import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.entity.CompensacionSalidaDepositoEntity;
import integra.asistencia.entity.TipoIncidencia;
import integra.asistencia.model.PausaAsistencia;
import integra.asistencia.query.CompensacionQuery;
import integra.asistencia.repository.AsistenciaRepository;
import integra.asistencia.repository.CompensacionRepository;
import integra.asistencia.repository.EmpleadoPuestoService;
import integra.asistencia.repository.PausaModelRepository;
import integra.asistencia.service.UnidadVerificadorService;
import integra.asistencia.service.WorkTimeImageService;
import integra.asistencia.util.CalculadoraJornada;
import integra.asistencia.util.HandlerExecutor;
import integra.unidad.repository.UnidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static integra.asistencia.util.ConvertidorPausas.mapearPausas;

@Service
@Transactional
public class FinalizarJornada extends BaseAsistenciaService implements HandlerExecutor<Void, FinalizarJornadaCommand> {

    private final AsistenciaRepository asistenciaRepository;
    private final PausaModelRepository pausaRepository;
    private final UnidadVerificadorService unidadVerificadorService;
    private final UnidadRepository unidadRepository;
    private final CompensacionRepository compensacionRepository;
    private final CompensacionDepositoService compensacionDepositoService;
    private final EmpleadoPuestoService empleadoPuestoService;

    public FinalizarJornada(WorkTimeImageService workTimeImageService, AsistenciaRepository asistenciaRepository, PausaModelRepository pausaRepository, UnidadVerificadorService unidadVerificadorService, UnidadRepository unidadRepository, CompensacionRepository compensacionRepository, CompensacionDepositoService compensacionDepositoService, EmpleadoPuestoService empleadoPuestoService) {
        super(workTimeImageService);
        this.asistenciaRepository = asistenciaRepository;
        this.pausaRepository = pausaRepository;
        this.unidadVerificadorService = unidadVerificadorService;
        this.unidadRepository = unidadRepository;
        this.compensacionRepository = compensacionRepository;
        this.compensacionDepositoService = compensacionDepositoService;
        this.empleadoPuestoService = empleadoPuestoService;
    }

    @Override
    public Void execute(FinalizarJornadaCommand command) {
        String pathFoto = guardarFotoSiExiste(command.foto(), command.empleadoId());
        boolean incidenciaUnidad = hayIncidenciaUnidad(command);

        AsistenciaModel asistencia = obtenerJornadaActiva(command.empleadoId());

        cerrarPausaActivaSiExiste(command.empleadoId());
        actualizarInconsistencia(asistencia, incidenciaUnidad);

        LocalDateTime horaFin = determinarHoraFin(command, asistencia);
        if (command.hora() != null) {
            asistencia.setComentario("El registro fue agregado manualmente");
        }

        // Check for early exit incidence for VN
        final Integer puestoId = asistencia.getEmpleado().getPuesto().getId();
        final boolean esNocturno = empleadoPuestoService.tienePuestoNocturno(puestoId);
        if (esNocturno && horaFin.toLocalTime().isBefore(LocalTime.of(6, 0))) {
            registrarIncidenciaSalidaAnticipada(asistencia, command, pathFoto);
        }

        finalizarJornada(asistencia, horaFin, pathFoto);

        if (incidenciaUnidad) {
            registrarIncidencia(asistencia, command, pathFoto);
        }
        return null;
    }

    private boolean hayIncidenciaUnidad(FinalizarJornadaCommand command) {
        return !Objects.equals(command.unidadId(), command.unidadAsignadaId());
    }

    private AsistenciaModel obtenerJornadaActiva(Integer empleadoId) {
        return asistenciaRepository.findFirstByEmpleado_IdAndJornadaCerradaFalseOrderByInicioJornadaDesc(empleadoId)
                .orElseThrow(() -> new RuntimeException("No hay jornada activa para el empleado: " + empleadoId));
    }

    private void cerrarPausaActivaSiExiste(Integer empleadoId) {
        pausaRepository.findFirstByAsistencia_Empleado_IdAndFinNullOrderByInicioDesc(empleadoId)
                .ifPresent(pausaActiva -> {
                    pausaActiva.setFin(LocalDateTime.now());
                    pausaRepository.save(pausaActiva);
                });
    }

    private void actualizarInconsistencia(AsistenciaModel asistencia, boolean incidenciaUnidad) {
        boolean yaInconsistente = Boolean.TRUE.equals(asistencia.getInconsistencia());
        asistencia.setInconsistencia(yaInconsistente || incidenciaUnidad);
    }

    private LocalDateTime procesarCompensacionDeposito(AsistenciaModel asistencia, FinalizarJornadaCommand command) {
        LocalDateTime horaFin = LocalDateTime.now();

        Optional<CompensacionQuery> configuracionCompensacion = unidadRepository.findById(command.unidadId(), CompensacionQuery.class);

        if (configuracionCompensacion.isEmpty()) {
            return horaFin;
        }
        List<PausaAsistencia> pausas = mapearPausas(asistencia.getPausas());
        // Usar CalculadoraJornada para calcular minutos faltantes considerando pausas
        int minutosFaltantes = CalculadoraJornada.calcularMinutosFaltantes(asistencia.getInicioJornada(), horaFin, pausas);
        if (minutosFaltantes > 0) {
            horaFin = aplicarCompensacion(asistencia, command, horaFin, minutosFaltantes, configuracionCompensacion.get()
                    .tiempoCompensacion());
        }
        return horaFin;
    }

    private LocalDateTime aplicarCompensacion(AsistenciaModel asistencia, FinalizarJornadaCommand command, LocalDateTime horaFin, int minutosFaltantes, LocalTime tiempoCompensacionUnidad) {
        int minutosCompensacionDisponibles = tiempoCompensacionUnidad.getHour() * 60 + tiempoCompensacionUnidad.getMinute();
        int minutosCompensacion = Math.min(minutosFaltantes, minutosCompensacionDisponibles);

        CompensacionSalidaDepositoEntity compensacion = compensacionDepositoService.crearCompensacion(asistencia, command, minutosFaltantes, minutosCompensacion);

        compensacionRepository.save(compensacion);

        // Actualizar la asistencia con el tiempo compensado
        asistencia.setTiempoCompensado(convertirMinutosALocalTime(minutosCompensacion));

        return horaFin.plusMinutes(minutosCompensacion);
    }

    private LocalTime convertirMinutosALocalTime(int minutos) {
        int horas = minutos / 60;
        int minutosRestantes = minutos % 60;
        return LocalTime.of(horas, minutosRestantes);
    }

    private void finalizarJornada(AsistenciaModel asistencia, LocalDateTime horaFin, String pathFoto) {
        asistencia.setFinJornada(horaFin);
        asistencia.setPathFotoFin(pathFoto);
        asistencia.setJornadaCerrada(true);
        asistenciaRepository.save(asistencia);
    }

    private LocalDateTime determinarHoraFin(FinalizarJornadaCommand command, AsistenciaModel asistencia) {
        if (command.hora() != null) {

            return LocalDateTime.now().toLocalDate().atTime(command.hora());
        }
        return command.finDeposito() != null ? procesarCompensacionDeposito(asistencia, command) : LocalDateTime.now();
    }

    private void registrarIncidencia(AsistenciaModel asistencia, FinalizarJornadaCommand command, String pathFoto) {
        unidadVerificadorService.registrarIncidenciaKioscoAsync(asistencia.getId(), command.empleadoId(), command.unidadAsignadaId(), command.unidadId(), pathFoto, TipoIncidencia.UNIDAD_INCORRECTA, "Fin de la jornada");
    }

    private void registrarIncidenciaSalidaAnticipada(AsistenciaModel asistencia, FinalizarJornadaCommand command, String pathFoto) {
        unidadVerificadorService.registrarIncidenciaKioscoAsync(asistencia.getId(), command.empleadoId(), command.unidadAsignadaId(), command.unidadId(), pathFoto, TipoIncidencia.SALIDA_ANTICIPADA, "Salida anticipada antes de las 06:00");
    }
}