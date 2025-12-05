package integra.asistencia.model;

import integra.asistencia.util.JornadaUtil;
import integra.asistencia.util.TipoPausa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PausaAsistencia {
    private Integer id;
    private Asistencia asistencia;
    private TipoPausa tipoPausa;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String pathFotoInicio;
    private String pathFotoFin;

    public PausaAsistencia(Integer id) {
        this.id = id;
    }

    public TiempoTrabajo getDuracion() {
        if (fin == null) {
            return TiempoTrabajo.deMinutos(0);
        }
        return TiempoTrabajo.deMinutos((int) Duration.between(inicio, fin).toMinutes());
    }

    public String formatear() {
        if (fin == null) {
            return "En curso";
        }
        return JornadaUtil.formatearTiempoHorasMinutos(getDuracion());
    }

}
