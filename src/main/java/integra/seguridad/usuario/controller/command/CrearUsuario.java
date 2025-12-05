package integra.seguridad.usuario.controller.command;

import integra.security.dto.CreateUserRequest;
import integra.seguridad.usuario.actions.CrearUsuarioCommand;
import integra.seguridad.usuario.service.CrearUsuarioService;
import integra.utils.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("usuarios")
public class CrearUsuario {
    private final CrearUsuarioService userService;

    @PostMapping
    public ResponseEntity<ResponseData<?>> crearUsuario(@RequestBody @Valid CreateUserRequest userRequest) {
        var command = CrearUsuarioCommand.from(userRequest);
        userService.crearUsuario(command);
        return ResponseEntity.ok(ResponseData.of(true, "Usuario registrado exitosamente"));

    }
}