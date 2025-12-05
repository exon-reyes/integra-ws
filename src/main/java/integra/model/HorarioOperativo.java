package integra.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class HorarioOperativo extends Operatividad {
    private Integer idHorario;
    private LocalTime apertura;
    private LocalTime cierre;
    private Unidad unidad;

    public HorarioOperativo(Integer id, String nombre, LocalTime apertura, LocalTime cierre) {
        super(null, nombre);
        this.idHorario = id;
        this.apertura = apertura;
        this.cierre = cierre;
    }

}