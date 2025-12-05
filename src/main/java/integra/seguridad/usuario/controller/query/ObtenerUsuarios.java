package integra.seguridad.usuario.controller.query;

import integra.model.Usuario;
import integra.security.service.UserService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class ObtenerUsuarios {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ResponseData<List<Usuario>>> obtenerUsuarios() {
        return ResponseEntity.ok(ResponseData.of(userService.obtenerInfoBasicaUsuarios(), "Usuarios"));
    }
}