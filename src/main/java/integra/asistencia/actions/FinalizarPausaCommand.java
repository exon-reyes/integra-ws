package integra.asistencia.actions;


import integra.asistencia.util.TipoPausa;

import java.time.LocalTime;

public record FinalizarPausaCommand(Integer empleadoId, TipoPausa pausa, String foto, Integer unidadId,
                                    Integer unidadAsignadaId, LocalTime hora) {
}
