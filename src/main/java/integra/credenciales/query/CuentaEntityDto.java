package integra.credenciales.query;

import integra.credenciales.entity.CuentaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * DTO for {@link CuentaEntity}
 */
@AllArgsConstructor
@Getter
public class CuentaEntityDto {
    private Integer id;

    private Integer tipoId;
    private String tipoNombre;

    private Integer departamentoId;
    private String departamentoNombre;

    private Integer unidadId;
    private String unidadClave;
    private String unidadNombreCompleto;

    private String usuario;
    private String clave;
    private String nota;
    private LocalDateTime creado;
    private LocalDateTime actualizado;
}