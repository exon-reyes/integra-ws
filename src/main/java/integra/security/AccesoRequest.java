package integra.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO para solicitud de acceso/login al sistema
 */
public record AccesoRequest(
        @NotNull(message = "El usuario no puede ser nulo.") @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres.") String username,
        @NotNull(message = "La contraseña no puede ser nula.") @Size(min = 1, max = 100, message = "La contraseña debe tener entre 1 y 100 caracteres.") String password) implements Serializable {

}