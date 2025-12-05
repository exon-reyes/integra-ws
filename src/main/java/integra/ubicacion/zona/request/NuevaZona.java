package integra.ubicacion.zona.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NuevaZona {
    @NotBlank(message = "La zona no debe estar en blanco")
    @NotNull(message = "Debe establecer un nombre para la zona")
    private String nombre;
}