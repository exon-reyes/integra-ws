package integra.seguridad.rol.controller.command;

import integra.seguridad.rol.service.EliminarRolService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class EliminarRol {
    private final EliminarRolService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> eliminarRol(@PathVariable Long id) {
        service.eliminarRol(id);
        return ResponseEntity.ok(ResponseData.of(true, "Rol eliminado"));
    }
}