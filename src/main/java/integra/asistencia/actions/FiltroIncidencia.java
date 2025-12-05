package integra.asistencia.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FiltroIncidencia {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer empleadoId;
    private Integer unidadId;
    private Integer zonaId;
    private Integer supervisorId;
}
