package integra.seguridad.rol.controller.command;

import integra.seguridad.rol.command.RolCommand;
import integra.seguridad.rol.model.Rol;
import integra.seguridad.rol.service.AgregarRolService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class AgregarRol {
    private final AgregarRolService service;

    @PostMapping()
    public ResponseEntity<ResponseData<Rol>> agregarRol(@RequestBody RolCommand command) {
        return ResponseEntity.ok(ResponseData.of(service.agregarRol(command), "Rol agregado"));
    }
}