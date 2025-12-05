package integra.seguridad.rol.controller.command;

import integra.seguridad.rol.command.RolCommand;
import integra.seguridad.rol.model.Rol;
import integra.seguridad.rol.service.ActualizarRolService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class ActualizarRol {
    private final ActualizarRolService service;

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<ResponseData<Rol>> actualizarRol(@PathVariable Long id, @RequestBody RolCommand command) {
        int result = service.actualizarNombreRol(id, command);
        if (result == 0) {
            return ResponseEntity.badRequest().body(ResponseData.of(null, "No se pudo actualizar el rol"));
        } else {
            return ResponseEntity.ok(ResponseData.of(new Rol(id, command.getNombre(), command.getDescripcion()), "Rol actualizado"));
        }
    }
}