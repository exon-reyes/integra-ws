package integra.seguridad.usuario.controller.command;

import integra.seguridad.usuario.service.EliminarUsuarioService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("usuarios")
@RequiredArgsConstructor
@RestController
public class EliminarUsuario {
    private final EliminarUsuarioService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> eliminarUsuario(@PathVariable Long id) {
        service.execute(id);
        return ResponseEntity.ok(ResponseData.of(null, "Usuario eliminado"));
    }
}
