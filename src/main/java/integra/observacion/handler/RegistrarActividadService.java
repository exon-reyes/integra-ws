package integra.observacion.handler;

import integra.observacion.constants.AccionHistorial;
import integra.observacion.entity.HistorialObservacionRepository;
import integra.observacion.entity.ObservacionHistorial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrarActividadService {
    private final HistorialObservacionRepository repository;

    public void registrarActividad(Integer observacionId, AccionHistorial accion, String actividad, String usuario) {
        var historial = new ObservacionHistorial(observacionId, accion.name(), actividad, usuario);
        repository.save(historial);
    }
}
