package integra.seguridad.rol.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RolCommand {
    private String nombre;
    private String descripcion;
    private List<String> idPermisos;
}
