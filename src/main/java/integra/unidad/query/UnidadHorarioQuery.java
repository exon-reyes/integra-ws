package integra.unidad.query;

import java.io.Serializable;
import java.time.LocalTime;

public record UnidadHorarioQuery(Integer id, String operatividadNombre, LocalTime apertura,
                                 LocalTime cierre) implements Serializable {
}