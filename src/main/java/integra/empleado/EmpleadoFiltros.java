package integra.empleado;

import lombok.Data;

@Data
public class EmpleadoFiltros {
    private String nombreCompleto;
    private Integer idSupervisor;
    private Integer idZona;
    private String clave;
    private String puesto;
    private String unidad;
    private Integer unidadId;
    private String estado;
}