package integra.seguridad.rol.controller.command;

import integra.seguridad.rol.command.ActualizarPermisosCommand;
import integra.seguridad.rol.service.ActualizarPermisosRolService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class ActualizarPermisosRol {
    private final ActualizarPermisosRolService service;

    @PutMapping("/{id}/permisos")
    public ResponseEntity<ResponseData<String>> actualizarPermisosRol(@PathVariable Long id, @RequestBody ActualizarPermisosCommand permisosIds) {
        permisosIds.setRolId(id);
        service.actualizarPermisosRol(permisosIds);
        return ResponseEntity.ok(ResponseData.of("OK", "Permisos actualizados correctamente"));
    }
}