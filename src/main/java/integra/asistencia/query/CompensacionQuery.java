package integra.asistencia.query;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link integra.unidad.entity.UnidadEntity}
 */
public record CompensacionQuery(Integer id, LocalTime tiempoCompensacion) implements Serializable {
}