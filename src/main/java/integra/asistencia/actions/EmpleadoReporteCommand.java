package integra.asistencia.actions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmpleadoReporteCommand {
    private Integer empleadoId;
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private Integer unidadId;
    private Integer zonaId;
    private Integer supervisorId;
    private Integer puestoId;
}
