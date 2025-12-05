package integra.seguridad.usuario.controller.query;

import integra.model.Usuario;
import integra.seguridad.usuario.service.ObtenerPrivilegiosUsuario;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class ObtenerPermisos {
    private final ObtenerPrivilegiosUsuario service;

    @GetMapping("/{id}/permisos")
    public ResponseEntity<ResponseData<Usuario>> obtenerPrivilegios(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseData.of(service.execute(id), "Usuario"));
    }
}