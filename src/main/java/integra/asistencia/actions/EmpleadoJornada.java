package integra.asistencia.actions;

public record EmpleadoJornada(Integer id, String clave, String nombre, boolean jornadaIniciada, boolean esNocturno,
                              String tipoPausa, Integer unidadAsignadaId) {
}