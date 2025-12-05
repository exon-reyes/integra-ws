package integra.asistencia.model;

import integra.model.Empleado;
import integra.model.Unidad;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class Compensacion {
    private Empleado empleado;
    private LocalDate fecha;
    private LocalTime horaSalida;
    private LocalTime tiempoCompensado;
    private Unidad unidadRegistro;
    private LocalTime horasTrabajadas;
    private LocalTime horasFaltante;
}
