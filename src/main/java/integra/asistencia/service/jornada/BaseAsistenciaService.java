package integra.asistencia.service.jornada;

import integra.asistencia.service.WorkTimeImageService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class BaseAsistenciaService {
    protected final WorkTimeImageService workTimeImageService;

    protected String guardarFotoSiExiste(String foto, Integer empleadoId) {
        if (foto == null) {
            return null;
        }
        try {
            return workTimeImageService.saveImg(foto, empleadoId);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen, contacte al administrador", e);
        }
    }
}