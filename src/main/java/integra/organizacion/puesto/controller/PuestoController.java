package integra.organizacion.puesto.controller;

import integra.organizacion.puesto.response.PuestoDto;
import integra.organizacion.puesto.service.PuestoService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("puestos")
public class PuestoController {
    private final PuestoService puestoService;

    @GetMapping
    public ResponseEntity<ResponseData<List<PuestoDto>>> obtenerPuestos() {
        return ResponseEntity.ok(ResponseData.of(puestoService.obtenerPuestos(), "Puestos"));
    }
}