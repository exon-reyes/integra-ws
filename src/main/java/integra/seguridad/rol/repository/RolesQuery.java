package integra.seguridad.rol.repository;

import integra.seguridad.rol.entity.Role;

import java.io.Serializable;

/**
 * DTO for {@link Role}
 */
public record RolesQuery(Long id, String name, String description, Boolean isDefault) implements Serializable {
}