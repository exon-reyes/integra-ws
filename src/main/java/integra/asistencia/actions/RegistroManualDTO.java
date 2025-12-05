package integra.asistencia.actions;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class RegistroManualDTO {
    @NotNull
    private Integer empleadoId;
    @NotNull
    private String tipoAccion; // "iniciarJornada", "finalizarJornada", "finalizarJornadaDeposito", "iniciarPausa", "finalizarPausa"
    @NotNull
    private LocalTime hora;
    private String pausa; // "COMIDA", "OTRA"
    @NotNull
    private String observaciones;
    @NotNull
    private Integer unidadId;
    private Integer unidadAsignadaId;
}