package integra.asistencia.query;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link integra.unidad.entity.UnidadEntity}
 */
public record KioscoInfo(Integer id, String nombreCompleto, Boolean requiereCamara,
                         String codigoAutorizacionKiosco, Boolean requiereCodigo, Integer versionKiosco,
                         LocalTime tiempoCompensacion) implements Serializable {
}