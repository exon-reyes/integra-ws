package integra.asistencia.query;

import java.time.LocalDate;

public record AsistenciaProyeccion(
        Integer empleadoId,
        String clave,
        String nombreCompleto,
        String puestoNombre,
        String unidadNombreCompleto,
        String zonaNombre,
        String supervisorNombre,
        LocalDate fecha,
        Boolean asistencia) {
}
