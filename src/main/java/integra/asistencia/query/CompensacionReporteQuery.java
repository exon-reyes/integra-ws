package integra.asistencia.query;

import java.time.LocalDate;
import java.time.LocalTime;

public record CompensacionReporteQuery(
        String clave,
        String colaborador,
        String unidad,
        LocalDate fecha,
        LocalTime horaSalida,
        LocalTime horasTrabajadas,
        LocalTime horasFaltantes,
        LocalTime tiempoCompensado
) {
}