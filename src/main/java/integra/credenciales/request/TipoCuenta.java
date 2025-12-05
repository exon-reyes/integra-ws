package integra.credenciales.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TipoCuenta(Integer id,
                         @NotNull(message = "Especifique un nombre") @NotBlank(message = "El tipo de cuenta no puede estar vacío") String nombre) {
}
