package integra.asistencia.util;

import integra.asistencia.model.PausaAsistencia;
import integra.asistencia.model.TiempoTrabajo;

import java.time.LocalDateTime;
import java.util.List;

public class CalculadoraJornadaExtension {

    /**
     * Calcula cuántos minutos faltan para completar las 8 horas estándar.
     * Retorna 0 si ya se completaron o excedieron las 8 horas.
     */
    public static int calcularMinutosFaltantes(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        TiempoTrabajo diferencia = CalculadoraJornada.calcularDiferenciaTiempo(inicioJornada, finJornada, pausas);
        return diferencia.minutos() < 0 ? Math.abs(diferencia.minutos()) : 0;
    }
}