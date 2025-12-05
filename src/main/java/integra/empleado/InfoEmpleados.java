package integra.empleado;

import java.io.Serializable;

/**
 * DTO for {@link EmpleadoEntity}
 */
public record InfoEmpleados(Integer id, String codigoEmpleado, String estatus,
                            String nombreCompleto) implements Serializable {
}