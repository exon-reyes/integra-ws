package integra.unidad.request;

import integra.unidad.entity.UnidadEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * DTO for {@link UnidadEntity}
 */
@Getter
public class ActualizarUnidad extends NuevaUnidad {
    @NotNull(message = "EL ID de la unidad no puede ser nulo")
    @Min(value = 1, message = "EL ID de la unidad debe ser mayor a 0")
    private final Integer id;

    public ActualizarUnidad(Integer id, String clave, String nombre, String localizacion, String telefono, Boolean activo, Boolean operativa, String direccion, String email, Integer idZona, Integer idEstado) {
        super(clave, nombre, localizacion, telefono, activo, direccion, email, idZona, idEstado);
        this.id = id;
    }
}
