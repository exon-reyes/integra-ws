package integra.observacion;

import integra.observacion.entity.Observacion;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Observacion}
 */
public record ResumenObservacion(Integer id, String folioObservacion, Integer unidadId, String unidadNombreCompleto,
                                 String tipoObservacionNombre, String estatusNombre, String estatusColorBg,
                                 String departamentoOrigenNombre, String titulo, String prioridad,
                                 Boolean requiereSeguimiento, LocalDateTime creado,
                                 LocalDateTime modificado) implements Serializable {

}