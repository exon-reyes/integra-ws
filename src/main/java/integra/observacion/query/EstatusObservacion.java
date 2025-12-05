package integra.observacion.query;

import integra.observacion.entity.Observacion;

import java.io.Serializable;

/**
 * DTO for {@link Observacion}
 */
public record EstatusObservacion(Integer estatusId, Boolean estatusEsFinal) implements Serializable {
}