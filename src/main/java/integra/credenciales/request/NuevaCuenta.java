package integra.credenciales.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NuevaCuenta {
    @NotNull(message = "Debe especificar el usuario")
    @NotBlank(message = "El usuario no debe estar vacío")
    private String usuario;
    private String clave;
    @NotNull(message = "Debe especificar el tipo de cuenta")
    private Integer idTipoCuenta;
    @NotNull(message = "Debe especificar el departamento asociado a la cuenta")
    private Integer idDepartamento;
    @NotNull(message = "Asigne una unidad a las credenciales")
    private Integer idUnidad;
    private String nota;

}
