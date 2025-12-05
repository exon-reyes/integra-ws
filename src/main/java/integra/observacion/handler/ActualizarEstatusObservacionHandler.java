package integra.observacion.handler;

import integra.observacion.ObservacionRepository;
import integra.observacion.constants.AccionHistorial;
import integra.observacion.constants.EstatusIncidente;
import integra.observacion.entity.Estatus;
import integra.observacion.service.ObservacionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ActualizarEstatusObservacionHandler {
    private final ObservacionRepository observacionRepository;
    private final ObservacionValidator observacionValidator;
    private final RegistrarActividadService actividadService;

    public void actualizarEstatusObservacion(Integer id, Integer estatusId, String usuario) {
        observacionValidator.checkCambioEstatus(id, estatusId);
        observacionRepository.updateEstatusById(new Estatus(estatusId), id);
        actividadService.registrarActividad(id, AccionHistorial.ESTADO_CAMBIADO, AccionHistorial.ESTADO_CAMBIADO.getDescripcion() + " a " + EstatusIncidente.fromId(estatusId)
                .getNombre(), usuario);

    }
}
