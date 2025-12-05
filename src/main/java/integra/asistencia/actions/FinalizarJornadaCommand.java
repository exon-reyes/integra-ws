package integra.asistencia.actions;

import java.time.LocalTime;

public record FinalizarJornadaCommand(Integer empleadoId, String foto, Integer unidadId, Boolean finDeposito,
                                      Integer unidadAsignadaId, LocalTime hora) {
}
