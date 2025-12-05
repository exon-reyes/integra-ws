package integra.asistencia.service.jornada;

import integra.asistencia.actions.IniciarPausaCommand;
import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.entity.PausaModel;
import integra.asistencia.entity.TipoIncidencia;
import integra.asistencia.repository.AsistenciaRepository;
import integra.asistencia.repository.PausaModelRepository;
import integra.asistencia.service.UnidadVerificadorService;
import integra.asistencia.service.ValidarRegistrarPausaService;
import integra.asistencia.service.WorkTimeImageService;
import integra.asistencia.util.HandlerExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
public class IniciarPausa extends BaseAsistenciaService implements HandlerExecutor<Void, IniciarPausaCommand> {
    private final ValidarRegistrarPausaService validarRegistrarPausaService;
    private final AsistenciaRepository asistenciaRepository;
    private final PausaModelRepository pausaRepository;
    private final UnidadVerificadorService unidadVerificadorService;

    public IniciarPausa(WorkTimeImageService workTimeImageService, ValidarRegistrarPausaService validarRegistrarPausaService, AsistenciaRepository asistenciaRepository, PausaModelRepository pausaRepository, UnidadVerificadorService unidadVerificadorService) {
        super(workTimeImageService);
        this.validarRegistrarPausaService = validarRegistrarPausaService;
        this.asistenciaRepository = asistenciaRepository;
        this.pausaRepository = pausaRepository;
        this.unidadVerificadorService = unidadVerificadorService;
    }


    @Override
    public Void execute(IniciarPausaCommand data) {
        validarRegistrarPausaService.execute(data.empleadoId());
        String pathFoto = guardarFotoSiExiste(data.foto(), data.empleadoId());
        AsistenciaModel asistencia = asistenciaRepository.findFirstByEmpleado_IdAndJornadaCerradaFalseOrderByInicioJornadaDesc(data.empleadoId())
                .orElseThrow(() -> new RuntimeException("No hay jornada activa"));

        boolean existeIncidencia = !Objects.equals(data.unidadId(), data.unidadAsignadaId());
        if (existeIncidencia && !asistencia.getInconsistencia()) {
            asistencia.setInconsistencia(true);
        }
        PausaModel pausa = new PausaModel();
        pausa.setAsistencia(asistencia);
        pausa.setTipo(data.tipo().toString());
        pausa.setInicio(determinarHoraInicio(data));
        pausa.setPathFotoInicio(pathFoto);
        pausaRepository.save(pausa);

        if (existeIncidencia) {
            unidadVerificadorService.registrarIncidenciaKioscoAsync(asistencia.getId(), data.empleadoId(), data.unidadAsignadaId(), data.unidadId(), pathFoto, TipoIncidencia.UNIDAD_INCORRECTA, "Inicio de pausa");
        }
        return null;
    }

    private LocalDateTime determinarHoraInicio(IniciarPausaCommand data) {
        if (data.hora() != null) {
            return LocalDateTime.now().toLocalDate().atTime(data.hora());
        }
        return LocalDateTime.now();
    }
}
