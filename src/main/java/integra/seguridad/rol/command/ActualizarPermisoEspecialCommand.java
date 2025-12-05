package integra.seguridad.rol.command;

import lombok.Data;

import java.util.List;

@Data
public class ActualizarPermisoEspecialCommand {
    private Long id;
    private List<Long> permisosIds;
}
