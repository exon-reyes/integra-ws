package integra.model;

import integra.credenciales.entity.CuentaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link CuentaEntity}
 */
@AllArgsConstructor
@Getter
public class Cuenta implements Serializable {
    private final Integer id;
    private final String usuario;
    private final String clave;
    private final String nota;
    private final LocalDateTime creado;
    private final LocalDateTime actualizado;
    private TipoCuenta tipo;
    private Departamento departamento;
    private Unidad unidad;
}