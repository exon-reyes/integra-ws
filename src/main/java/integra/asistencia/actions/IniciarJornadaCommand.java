package integra.asistencia.actions;

import java.time.LocalTime;

public record IniciarJornadaCommand(Integer empleadoId, String foto, Integer unidadId, Integer unidadAsignadaId,
                                    LocalTime hora) {
}
