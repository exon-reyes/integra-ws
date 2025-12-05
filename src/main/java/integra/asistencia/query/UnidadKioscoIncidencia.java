package integra.asistencia.query;

import integra.unidad.entity.UnidadEntity;

import java.io.Serializable;

/**
 * DTO for {@link UnidadEntity}
 */
public record UnidadKioscoIncidencia(String nombreCompleto) implements Serializable {
}