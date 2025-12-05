package integra.unidad.query;

import integra.unidad.entity.UnidadEntity;

import java.io.Serializable;

/**
 * DTO for {@link UnidadEntity}
 */

public record UnidadContactoQuery(Integer id, String clave, String email, String nombre, String nombreCompleto,
                                  String direccion, String localizacion, boolean activo, String telefono,
                                  Integer zonaId, String zonaNombre, Integer estadoId,
                                  String estadoNombre, String supervisorNombreCompleto) implements Serializable {
}