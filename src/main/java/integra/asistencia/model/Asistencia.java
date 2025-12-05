package integra.asistencia.model;

import integra.asistencia.util.CalculadoraJornada;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static integra.asistencia.util.JornadaUtil.formatearDiferencia;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class Asistencia {
    private Integer id;
    private LocalDate fecha;
    private LocalDateTime inicioJornada;
    private LocalDateTime finJornada;
    private Boolean jornadaCerrada;
    private String pathFotoInicio;
    private String pathFotoFin;
    private List<PausaAsistencia> pausas;
    private String comentario;
    private Boolean cerradoAutomatico;
    private Boolean inconsistencia;
    private LocalTime tiempoCompensado;
    private Boolean fueAsistenciaNocturna;

    public Asistencia(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el tiempo bruto trabajado en una jornada.
     * <p>
     * Este método calcula el tiempo total, en términos de horas trabajadas, desde el inicio hasta el final de la jornada.
     * Si el cierre fue automático, retorna cero ya que la hora de cierre se dió como referencia.
     *
     * @return TiempoTrabajo objeto que representa las horas brutas trabajadas en la jornada.
     */
    public TiempoTrabajo getHorasBrutasTrabajadas() {
        return CalculadoraJornada.calcularTiempoBruto(inicioJornada, finJornada, cerradoAutomatico);
    }


    public TiempoTrabajo getHorasNetasTrabajadas() {
        return CalculadoraJornada.calcularTiempoNeto(inicioJornada, finJornada, pausas, cerradoAutomatico);
    }

    public TiempoTrabajo getTotalPausas() {
        return CalculadoraJornada.calcularTiempoTotalPausas(pausas);
    }

    public TiempoTrabajo getTotalPausaComida() {
        return CalculadoraJornada.calcularTiempoPausaComida(pausas);
    }

    public TiempoTrabajo getTotalOtrasPausas() {
        return CalculadoraJornada.calcularTiempoOtrasPausas(pausas);
    }

    public TiempoTrabajo getHorasExtras() {
        return CalculadoraJornada.calcularTiempoExtras(inicioJornada, finJornada, pausas, cerradoAutomatico);
    }

    public int getHorasExtrasNetas() {
        return CalculadoraJornada.calcularTiempoExtras(inicioJornada, finJornada, pausas, cerradoAutomatico).getHoras();
    }

    public String getDiferencia8HorasTrabajadasFormateada() {
        // Si fue cerrado automáticamente, no hay diferencia que calcular
        if (Boolean.TRUE.equals(cerradoAutomatico)) {
            return "Cierre automático";
        }

        TiempoTrabajo diferencia = CalculadoraJornada.calcularDiferenciaTiempo(inicioJornada, finJornada, pausas);
        TiempoTrabajo tiempoNeto = CalculadoraJornada.calcularTiempoNeto(inicioJornada, finJornada, pausas);
        TiempoTrabajo jornadaRegular = TiempoTrabajo.deHoras(8);

        if (tiempoNeto.esMayorQue(jornadaRegular)) {
            return formatearDiferencia(diferencia, true);
        } else if (tiempoNeto.esMenorQue(jornadaRegular)) {
            TiempoTrabajo tiempoFaltante = jornadaRegular.restar(tiempoNeto);
            return formatearDiferencia(tiempoFaltante, false);
        } else {
            return "";
        }
    }
}