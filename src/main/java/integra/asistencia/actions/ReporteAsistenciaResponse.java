package integra.asistencia.actions;

import java.time.LocalDate;
import java.util.List;

public record ReporteAsistenciaResponse(
        List<LocalDate> fechas,
        List<EmpleadoAsistencia> empleados) {
}
