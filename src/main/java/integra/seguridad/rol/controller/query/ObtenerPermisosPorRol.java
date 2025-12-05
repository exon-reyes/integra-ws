package integra.seguridad.rol.controller.query;

import integra.model.Permiso;
import integra.seguridad.rol.service.ObtenerPermisosPorRolService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class ObtenerPermisosPorRol {
    private final ObtenerPermisosPorRolService service;

    @GetMapping("/{id}/permisos")
    public ResponseEntity<ResponseData<List<Permiso>>> obtenerPermisosPorRol(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseData.of(service.obtenerPermisosPorRol(id), "Roles"));
    }
}