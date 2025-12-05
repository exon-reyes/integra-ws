package integra.empleado;

import java.util.List;

public interface EmpleadoRepositoryCustom {
    List<InfoBasicaEmpleado> findWithFilters(EmpleadoFiltros filtros);
}