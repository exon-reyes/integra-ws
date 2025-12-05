package integra.seguridad.usuario.actions;

import integra.security.dto.CreateUserRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrearUsuarioCommand {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private List<Long> roles;

    public static CrearUsuarioCommand from(CreateUserRequest request) {
        var command = new CrearUsuarioCommand();
        command.setUsername(request.getUsername());
        command.setPassword(request.getPassword());
        command.setEmail(request.getEmail());
        command.setFullname(request.getFullname());
        command.setRoles(request.getRoles());

        return command;

    }
}