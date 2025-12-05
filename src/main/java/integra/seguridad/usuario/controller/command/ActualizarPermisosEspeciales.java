package integra.seguridad.usuario.controller.command;

import integra.seguridad.rol.command.ActualizarPermisoEspecialCommand;
import integra.seguridad.usuario.service.ActualizarPermisosEspecialesService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class ActualizarPermisosEspeciales {
    private final ActualizarPermisosEspecialesService userService;

    @PutMapping("/{id}/permisos")
    public ResponseEntity<ResponseData<Void>> habilitarUsuario(@PathVariable Long id, @RequestBody ActualizarPermisoEspecialCommand command) {
        command.setId(id);
        userService.actualizarPermisosEspeciales(command);
        return ResponseEntity.ok(ResponseData.of(null, "Usuario habilitado"));
    }
}