package integra.asistencia.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link IncidenciaKioscoEntity}
 */
public record Incidencia(String tipoIncidencia, String mensaje, LocalDateTime fecha,
                         String pathImagen) implements Serializable {
}