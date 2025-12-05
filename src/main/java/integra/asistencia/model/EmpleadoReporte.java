package integra.asistencia.model;

import integra.asistencia.util.CalculadoraJornada;
import integra.model.Empleado;
import integra.model.Puesto;
import integra.model.Unidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase que representa un reporte de empleado con información de asistencia y cálculos derivados.
 * Esta clase encapsula los datos de un empleado junto con su información de asistencia durante
 * un período específico y proporciona métodos para calcular métricas importantes como tiempo
 * trabajado, horas extras y días trabajados.
 */
@Getter
@AllArgsConstructor
@Setter
public class EmpleadoReporte {
    /**
     * Identificador único del reporte de empleado.
     */
    private Integer id;

    /**
     * Datos del empleado al que pertenece este reporte.
     * Contiene información básica del empleado como nombre, código, etc.
     */
    private Empleado empleado;

    /**
     * Unidad organizacional a la que pertenece el empleado.
     * Representa la división o departamento dentro de la organización.
     */
    private Unidad unidad;

    /**
     * Puesto que ocupa el empleado.
     * Contiene información sobre la posición laboral del empleado.
     */
    private Puesto puesto;

    /**
     * Lista de registros de asistencia del empleado.
     * Contiene los datos de asistencia del empleado durante el período del reporte.
     */
    private List<Asistencia> asistencias;

    /**
     * Fecha de inicio del período cubierto por el reporte.
     * Representa el límite inferior del rango de fechas incluidas en el reporte.
     */
    private LocalDate fechaDesde;

    /**
     * Fecha de finalización del período cubierto por el reporte.
     * Representa el límite superior del rango de fechas incluidas en el reporte.
     */
    private LocalDate fechaHasta;

    /**
     * Calcula la sumatoria del tiempo neto trabajado por el empleado.
     * Utiliza la clase {@link CalculadoraJornada} para obtener el tiempo neto redondeado
     * basado en los registros de asistencia del empleado.
     *
     * @return la sumatoria total del tiempo neto trabajado en minutos
     * @see CalculadoraJornada#getSumatoriaTiempoNetoRedondeado(List)
     */
    public int getSumatoriaTiempoTrabajado() {
        return CalculadoraJornada.getSumatoriaTiempoNetoRedondeado(asistencias);
    }

    /**
     * Calcula la sumatoria de tiempo extras trabajado por el empleado.
     * Utiliza la clase {@link CalculadoraJornada} para obtener el tiempo de horas extras
     * basado en los registros de asistencia del empleado.
     *
     * @return la sumatoria total del tiempo de horas extras trabajadas en minutos
     * @see CalculadoraJornada#getSumatoriaTiempoExtras(List)
     */
    public int getSumatoriaTiempoExtras() {
        return CalculadoraJornada.getSumatoriaTiempoExtras(asistencias);
    }

    /**
     * Calcula la cantidad de días únicos en los que el empleado ha trabajado.
     * Utiliza las restricciones de operación:
     * - NO se consideran días con cierre automático para cálculos
     * - SE considera como día trabajado si marcó entrada pero no salida
     *
     * @return la cantidad de días únicos trabajados durante el período del reporte
     */
    public long getDiasTrabajados() {
        return asistencias.stream()
                .filter(asistencia -> CalculadoraJornada.esDiaTrabajado(
                        asistencia.getInicioJornada(),
                        asistencia.getFinJornada(),
                        asistencia.getJornadaCerrada(),
                        asistencia.getCerradoAutomatico(),
                        asistencia.getPausas()
                ))
                .map(Asistencia::getFecha)
                .distinct()
                .count();
    }

    public int getTotalInconsistencias() {
        if (asistencias == null) return 0;
        return (int) asistencias.stream()
                .filter(asistencia -> Boolean.TRUE.equals(asistencia.getInconsistencia()))
                .count();
    }
}
