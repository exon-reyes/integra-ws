package integra.asistencia.actions;

import java.util.List;

public record EmpleadoAsistencia(
        String clave,
        String nombreCompleto,
        String nombreUnidad,
        String puesto,
        String zona,
        String supervisor,
        List<Integer> asistencias) {
}
