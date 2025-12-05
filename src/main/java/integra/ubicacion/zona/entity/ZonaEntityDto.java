package integra.ubicacion.zona.entity;

import java.io.Serializable;

/**
 * DTO for {@link ZonaEntity}
 */
public record ZonaEntityDto(Integer id, String nombre, Boolean activo) implements Serializable {
}