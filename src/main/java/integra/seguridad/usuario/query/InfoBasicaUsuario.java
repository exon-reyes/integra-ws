package integra.seguridad.usuario.query;

import integra.seguridad.usuario.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record InfoBasicaUsuario(Long id, String username, String email, Boolean enabled) implements Serializable {
}