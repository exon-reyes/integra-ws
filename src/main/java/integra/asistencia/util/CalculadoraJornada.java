package integra.asistencia.util;


import integra.asistencia.model.Asistencia;
import integra.asistencia.model.PausaAsistencia;
import integra.asistencia.model.TiempoTrabajo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


/**
 * Calculadora optimizada para métricas de jornada laboral.
 * <p>
 * Propósitos principales:
 * 1. Calcular horas brutas trabajadas entre dos períodos de LocalDateTime
 * 2. Calcular tiempo neto trabajado (contemplando pausas)
 * 3. Calcular total de tiempo pausado (Comida + Otros)
 * 4. Calcular diferencia respecto a 8 horas estándar (+5 min, -3 horas)
 * 5. Calcular horas extras brutas y netas (redondeando hacia abajo)
 */
public class CalculadoraJornada {

    private static final TiempoTrabajo JORNADA_ESTANDAR = TiempoTrabajo.deHoras(8);


    // === 1. CÁLCULO DE HORAS BRUTAS TRABAJADAS ===

    /**
     * Calcula las horas brutas trabajadas entre dos períodos de LocalDateTime.
     * Representa el tiempo total entre inicio y fin, sin descontar pausas.
     * Si el cierre fue automático, retorna cero ya que no se debe contemplar en los cálculos.
     */
    public static TiempoTrabajo calcularTiempoBruto(LocalDateTime inicio, LocalDateTime fin) {
        Duration duracion = calcularDuracion(inicio, fin);
        return TiempoTrabajo.deMinutos((int) duracion.toMinutes());
    }

    /**
     * Calcula las horas brutas trabajadas considerando si el cierre fue automático.
     * Si cerradoAutomatico es true, retorna cero.
     */
    public static TiempoTrabajo calcularTiempoBruto(LocalDateTime inicio, LocalDateTime fin, Boolean cerradoAutomatico) {
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return TiempoTrabajo.cero();
        }
        return calcularTiempoBruto(inicio, fin);
    }

    // === 2. CÁLCULO DE TIEMPO NETO TRABAJADO ===

    /**
     * Calcula cuánto trabajó realmente el empleado, contemplando las pausas.
     * Convierte el tiempo bruto a tiempo neto descontando todas las pausas.
     */
    public static TiempoTrabajo calcularTiempoNeto(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        TiempoTrabajo tiempoBruto = calcularTiempoBruto(inicioJornada, finJornada);
        TiempoTrabajo tiempoPausas = calcularTiempoTotalPausas(pausas);
        return tiempoBruto.restar(tiempoPausas);
    }

    /**
     * Calcula cuánto trabajó realmente el empleado, considerando si el cierre fue automático.
     * Si cerradoAutomatico es true, retorna cero.
     */
    public static TiempoTrabajo calcularTiempoNeto(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas, Boolean cerradoAutomatico) {
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return TiempoTrabajo.cero();
        }
        return calcularTiempoNeto(inicioJornada, finJornada, pausas);
    }

    // === 3. CÁLCULO DE TIEMPO PAUSADO ===

    /**
     * Calcula el total de tiempo pausado (Comida + Otros).
     */
    public static TiempoTrabajo calcularTiempoTotalPausas(List<PausaAsistencia> pausas) {
        if (pausas == null || pausas.isEmpty()) {
            return TiempoTrabajo.cero();
        }

        Duration totalPausas = pausas.stream()
                .map(CalculadoraJornada::calcularDuracionPausa)
                .reduce(Duration.ZERO,
                        Duration::plus);

        return TiempoTrabajo.deMinutos((int) totalPausas.toMinutes());
    }

    /**
     * Calcula el tiempo total de pausas de comida.
     */
    public static TiempoTrabajo calcularTiempoPausaComida(List<PausaAsistencia> pausas) {
        if (pausas == null || pausas.isEmpty()) {
            return TiempoTrabajo.cero();
        }

        Duration totalPausaComida = pausas.stream()
                .filter(CalculadoraJornada::esPausaComida)
                .map(CalculadoraJornada::calcularDuracionPausa)
                .reduce(Duration.ZERO,
                        Duration::plus);

        return TiempoTrabajo.deMinutos((int) totalPausaComida.toMinutes());
    }

    /**
     * Calcula el tiempo total de otras pausas (no comida).
     */
    public static TiempoTrabajo calcularTiempoOtrasPausas(List<PausaAsistencia> pausas) {
        if (pausas == null || pausas.isEmpty()) {
            return TiempoTrabajo.cero();
        }

        Duration totalOtrasPausas = pausas.stream()
                .filter(p -> !esPausaComida(p))
                .map(CalculadoraJornada::calcularDuracionPausa)
                .reduce(Duration.ZERO,
                        Duration::plus);

        return TiempoTrabajo.deMinutos((int) totalOtrasPausas.toMinutes());
    }


    // === 4. CÁLCULO DE DIFERENCIA RESPECTO A 8 HORAS ===

    /**
     * Calcula la diferencia de tiempo trabajado a partir de las 8 horas estándar.
     * Ejemplos: +5 min, -3 horas, +2 h 30 min
     */
    public static TiempoTrabajo calcularDiferenciaTiempo(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        TiempoTrabajo tiempoNeto = calcularTiempoNeto(inicioJornada,
                finJornada,
                pausas);
        return tiempoNeto.restar(JORNADA_ESTANDAR);
    }

    // === 5. CÁLCULO DE HORAS EXTRAS ===

    /**
     * Calcula las horas extras brutas (tiempo completo con minutos).
     * Solo si se exceden las 8 horas estándar.
     */
    public static TiempoTrabajo calcularTiempoExtras(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        TiempoTrabajo tiempoNeto = calcularTiempoNeto(inicioJornada, finJornada, pausas);
        return tiempoNeto.esMayorQue(JORNADA_ESTANDAR) ? tiempoNeto.restar(JORNADA_ESTANDAR) : TiempoTrabajo.cero();
    }

    /**
     * Calcula las horas extras considerando si el cierre fue automático.
     * Si cerradoAutomatico es true, retorna cero.
     */
    public static TiempoTrabajo calcularTiempoExtras(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas, Boolean cerradoAutomatico) {
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return TiempoTrabajo.cero();
        }
        return calcularTiempoExtras(inicioJornada, finJornada, pausas);
    }

    /**
     * Calcula las horas extras netas (redondeadas hacia abajo).
     * Si no se completa la hora, se redondea a la hora inferior.
     */
    public static int calcularHorasExtrasNetas(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        TiempoTrabajo tiempoExtras = calcularTiempoExtras(inicioJornada, finJornada, pausas);
        return tiempoExtras.getHoras(); // Redondeo hacia abajo automático
    }

    /**
     * Calcula las horas extras netas considerando si el cierre fue automático.
     */
    public static int calcularHorasExtrasNetas(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas, Boolean cerradoAutomatico) {
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return 0;
        }
        return calcularHorasExtrasNetas(inicioJornada, finJornada, pausas);
    }

    /**
     * Calcula las horas netas redondeadas (hacia abajo).
     */
    public static int calcularHorasNetasRedondeadas(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        TiempoTrabajo tiempoNeto = calcularTiempoNeto(inicioJornada, finJornada, pausas);
        return tiempoNeto.getHoras(); // Redondeo hacia abajo automático
    }

    /**
     * Calcula las horas netas redondeadas considerando si el cierre fue automático.
     */
    public static int calcularHorasNetasRedondeadas(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas, Boolean cerradoAutomatico) {
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return 0;
        }
        return calcularHorasNetasRedondeadas(inicioJornada, finJornada, pausas);
    }

    // === 6. CÁLCULOS DERIVADOS ===

    /**
     * Calcula el acumulado total (horas netas + horas extras redondeadas).
     */
    public static double calcularAcumulado(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        int horasNetas = calcularHorasNetasRedondeadas(inicioJornada, finJornada, pausas);
        int horasExtras = calcularHorasExtrasNetas(inicioJornada, finJornada, pausas);
        return horasNetas + horasExtras;
    }

    /**
     * Calcula el acumulado total considerando si el cierre fue automático.
     */
    public static double calcularAcumulado(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas, Boolean cerradoAutomatico) {
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return 0;
        }
        return calcularAcumulado(inicioJornada, finJornada, pausas);
    }
    // === MÉTODOS PARA COMPATIBILIDAD CON ASISTENCIA ===

    /**
     * Calcula la suma total de tiempo extras de múltiples asistencias.
     * Ya considera automáticamente las restricciones de cierre automático.
     */
    public static int getSumatoriaTiempoExtras(List<Asistencia> jornadas) {
        return jornadas.stream()
                .mapToInt(Asistencia::getHorasExtrasNetas)
                .sum();
    }

    /**
     * Calcula la suma total de tiempo neto redondeado de múltiples asistencias.
     * Ya considera automáticamente las restricciones de cierre automático.
     */
    public static int getSumatoriaTiempoNetoRedondeado(List<Asistencia> jornadas) {
        return jornadas.stream()
                .mapToInt(asistencia -> asistencia.getHorasNetasTrabajadas().getHoras())
                .sum();
    }

    // === MÉTODOS DE COMPATIBILIDAD (para no romper código existente) ===

    /**
     * @deprecated Usar calcularTiempoBruto que retorna TiempoTrabajo
     */
    @Deprecated
    public static Duration calcularDuracionBruta(LocalDateTime inicio, LocalDateTime fin) {
        return calcularDuracion(inicio,
                fin);
    }

    /**
     * @deprecated Usar calcularTiempoPausaComida que retorna TiempoTrabajo
     */
    @Deprecated
    public static Duration calcularDuracionPausaComida(List<PausaAsistencia> pausas) {
        return Duration.ofMinutes(calcularTiempoPausaComida(pausas).minutos());
    }

    /**
     * @deprecated Usar calcularTiempoOtrasPausas que retorna TiempoTrabajo
     */
    @Deprecated
    public static Duration calcularDuracionOtrasPausas(List<PausaAsistencia> pausas) {
        return Duration.ofMinutes(calcularTiempoOtrasPausas(pausas).minutos());
    }
    // === MÉTODOS AUXILIARES ===

    /**
     * Calcula la duración entre dos LocalDateTime
     */
    public static Duration calcularDuracion(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            return Duration.ZERO;
        }
        return Duration.between(inicio,
                fin);
    }

    private static boolean esPausaComida(PausaAsistencia pausa) {
        return pausa.getTipoPausa() != null && TipoPausa.COMIDA.equals(pausa.getTipoPausa());
    }

    private static Duration calcularDuracionPausa(PausaAsistencia pausa) {
        Objects.requireNonNull(pausa,
                "La pausa no puede ser null");
        return calcularDuracion(pausa.getInicio(),
                pausa.getFin());
    }

    /**
     * Calcula cuántos minutos faltan para completar las 8 horas estándar.
     * Retorna 0 si ya se completaron o excedieron las 8 horas.
     */
    public static int calcularMinutosFaltantes(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        TiempoTrabajo tiempoNeto = calcularTiempoNeto(inicioJornada, finJornada, pausas);
        int minutosNetos = tiempoNeto.getHoras() * 60 + tiempoNeto.getMinutosRestantes();
        int minutosEstandar = 8 * 60; // 480 minutos
        return minutosNetos < minutosEstandar ? minutosEstandar - minutosNetos : 0;
    }

    /**
     * Calcula cuántos minutos faltan considerando si el cierre fue automático.
     */
    public static int calcularMinutosFaltantes(LocalDateTime inicioJornada, LocalDateTime finJornada, List<PausaAsistencia> pausas, Boolean cerradoAutomatico) {
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return 480; // 8 horas completas faltan si fue cierre automático
        }
        return calcularMinutosFaltantes(inicioJornada, finJornada, pausas);
    }

    // === MÉTODOS PARA VERIFICAR DÍAS TRABAJADOS ===

    /**
     * Verifica si un día debe considerarse como trabajado.
     * Un día se considera trabajado si:
     * - Tiene entrada (independiente del cierre automático)
     * - Tiene horas netas trabajadas > 0 (excluye cierres automáticos para cálculos)
     */
    public static boolean esDiaTrabajado(LocalDateTime inicioJornada, LocalDateTime finJornada,
                                         Boolean jornadaCerrada, Boolean cerradoAutomatico,
                                         List<PausaAsistencia> pausas) {
        // Si marcó entrada, se considera día trabajado (incluye entrada sin salida y cierres automáticos)
        if (inicioJornada != null) {
            return true;
        }

        return false;
    }
}