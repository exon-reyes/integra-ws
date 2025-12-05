package integra.asistencia.repository;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.query.AsistenciaProyeccion;

import java.util.List;

public interface AsistenciaRepositoryCustom {
    List<AsistenciaProyeccion> findProyeccionByCriteria(EmpleadoReporteCommand request);
}
