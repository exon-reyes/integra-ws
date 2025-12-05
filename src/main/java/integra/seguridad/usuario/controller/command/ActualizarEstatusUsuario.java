package integra.seguridad.usuario.controller.command;

import integra.seguridad.usuario.service.ActualizarEstatusService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class ActualizarEstatusUsuario {
    private final ActualizarEstatusService userService;

    @PutMapping("/{id}/estatus")
    public ResponseEntity<ResponseData<String>> actualizarEstatus(@PathVariable Long id, @RequestParam boolean activo) {
        userService.execute(id, activo);
        return ResponseEntity.ok(ResponseData.of("OK", "Estatus actualizado"));
    }
}