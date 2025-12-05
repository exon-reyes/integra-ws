package integra.unidad.request;

import integra.unidad.entity.UnidadEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link UnidadEntity}
 */
@AllArgsConstructor
@Getter
public class NuevaUnidad implements Serializable {
    @NotNull(message = "La clave no puede ser nula.")
    @Size(min = 1, max = 10, message = "La clave debe tener entre 1 y 10 caracteres.")
    private final String clave;

    @NotNull(message = "El nombre no puede ser nulo.")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres.")
    private final String nombre;

    @Size(max = 80, message = "La localización no puede exceder los 80 caracteres.")
    private final String localizacion;

    @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres.")
    private final String telefono;

    private final Boolean activo;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private final String direccion;

    @Size(max = 100, message = "El email no puede exceder los 100 caracteres.")
    private final String email;

    @NotNull(message = "Debe especificar una zona")
    private final Integer idZona;

    @NotNull(message = "Debe especificar un estado")
    private final Integer idEstado;
}