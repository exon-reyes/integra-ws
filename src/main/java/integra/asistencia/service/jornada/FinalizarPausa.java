package integra.asistencia.service.jornada;


import integra.asistencia.actions.FinalizarPausaCommand;
import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.entity.PausaModel;
import integra.asistencia.entity.TipoIncidencia;
import integra.asistencia.repository.PausaModelRepository;
import integra.asistencia.service.UnidadVerificadorService;
import integra.asistencia.service.WorkTimeImageService;
import integra.asistencia.util.HandlerExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
public class FinalizarPausa extends BaseAsistenciaService implements HandlerExecutor<Void, FinalizarPausaCommand> {
    private final PausaModelRepository pausaRepository;
    private final UnidadVerificadorService unidadVerificadorService;

    public FinalizarPausa(WorkTimeImageService workTimeImageService, PausaModelRepository pausaRepository, UnidadVerificadorService unidadVerificadorService) {
        super(workTimeImageService);
        this.pausaRepository = pausaRepository;
        this.unidadVerificadorService = unidadVerificadorService;
    }

    @Override
    public Void execute(FinalizarPausaCommand data) {
        String pathFoto = guardarFotoSiExiste(data.foto(), data.empleadoId());
        PausaModel pausa = pausaRepository.findFirstByAsistencia_Empleado_IdAndFinNullOrderByInicioDesc(data.empleadoId())
                .orElseThrow(() -> new RuntimeException("No hay pausa activa para este empleado"));
        pausa.setFin(determinarHoraFin(data));
        pausa.setPathFotoFin(pathFoto);

        boolean hayIncidencia = !Objects.equals(data.unidadId(), data.unidadAsignadaId());
        if (hayIncidencia) {
            AsistenciaModel asistencia = pausa.getAsistencia();
            asistencia.setInconsistencia(true);
            pausa.setAsistencia(asistencia);
        }

        pausaRepository.save(pausa);

        if (hayIncidencia) {
            unidadVerificadorService.registrarIncidenciaKioscoAsync(pausa.getAsistencia()
                    .getId(), data.empleadoId(), data.unidadAsignadaId(), data.unidadId(), pathFoto, TipoIncidencia.UNIDAD_INCORRECTA, "Fin de la pausa");
        }
        return null;
    }

    private LocalDateTime determinarHoraFin(FinalizarPausaCommand data) {
        if (data.hora() != null) {
            return LocalDateTime.now().toLocalDate().atTime(data.hora());
        }
        return LocalDateTime.now();
    }
}
