package integra.seguridad.rol.command;

import lombok.Data;

import java.util.List;

@Data
public class ActualizarPermisosCommand {
    private Long rolId;
    private List<String> permisosIds;
}