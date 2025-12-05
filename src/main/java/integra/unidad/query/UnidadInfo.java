package integra.unidad.query;

import integra.unidad.entity.UnidadEntity;

/**
 * DTO for {@link UnidadEntity}
 */

public record UnidadInfo(Integer id, String clave, String nombre, Boolean activo, String zonaNombre,
                         String supervisorNombreCompleto) {

}