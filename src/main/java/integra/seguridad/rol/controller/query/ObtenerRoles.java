package integra.seguridad.rol.controller.query;

import integra.seguridad.rol.service.ObtenerRolesService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
public class ObtenerRoles {
    private final ObtenerRolesService service;

    @GetMapping()
    public ResponseEntity<ResponseData<List<?>>> obtenerRoles() {
        return ResponseEntity.ok(ResponseData.of(service.obtenerInfoRolesBasico(), "Roles"));
    }
}
