package integra.asistencia.actions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmpleadoReporteRequest {
    private Integer empleadoId;
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private Integer unidadId;
    private Integer supervisorId;
    private Integer zonaId;
    private Integer puestoId;
}
