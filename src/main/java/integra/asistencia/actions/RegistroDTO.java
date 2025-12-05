package integra.asistencia.actions;

import integra.asistencia.util.TipoPausa;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class RegistroDTO {
    @NotNull(message = "Se requiere una unidad a registrar")
    private Integer unidadId;
    private TipoPausa pausa;
    @NotNull(message = "El ID del empleado no puede ser nulo")
    private Integer empleadoId;
    private String foto;
    private Integer unidadAsignadaId;
    private Boolean finDeposito;
    private LocalTime hora;
}