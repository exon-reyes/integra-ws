package integra.asistencia.actions;


import integra.asistencia.util.TipoPausa;

import java.time.LocalTime;

public record IniciarPausaCommand(Integer empleadoId, TipoPausa tipo, String foto, Integer unidadId,
                                  Integer unidadAsignadaId, LocalTime hora) {
}
