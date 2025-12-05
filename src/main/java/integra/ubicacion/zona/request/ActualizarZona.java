package integra.ubicacion.zona.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActualizarZona {
    @NotNull(message = "Debe especificar el ID de la zona")
    private Integer id;

    @NotBlank(message = "Debe establecer un nombre para la zona")
    @NotNull(message = "Debe establecer un nombre para la zona")
    private String nombre;

    @NotNull(message = "Debe especificar el estado activo de la zona")
    private Boolean activo;
}