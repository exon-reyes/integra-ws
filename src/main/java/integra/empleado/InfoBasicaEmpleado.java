package integra.empleado;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link EmpleadoEntity}
 */
public record InfoBasicaEmpleado(Integer id, String codigoEmpleado, String puestoNombre, String estatus,
                                 String nombreCompleto, String unidadNombreCompleto, LocalDate fechaAlta,
                                 LocalDate fechaBaja) implements Serializable {
}