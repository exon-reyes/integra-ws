package integra.organizacion.puesto.response;

import java.io.Serializable;

/**
 * DTO for {@link integra.model.Puesto}
 */
public record PuestoDto(Integer id, String nombre, Boolean activo) implements Serializable {
}