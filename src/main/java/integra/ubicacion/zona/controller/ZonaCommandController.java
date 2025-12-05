package integra.ubicacion.zona.controller;

import integra.ubicacion.zona.request.ActualizarZona;
import integra.ubicacion.zona.request.NuevaZona;
import integra.ubicacion.zona.service.ZonaCommandService;
import integra.utils.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("zonas")
@RequiredArgsConstructor
class ZonaCommandController {
    private final ZonaCommandService zonaCommandService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> registrarZona(@Valid @RequestBody NuevaZona zona) {
        zonaCommandService.registrarZona(zona);
        return ResponseEntity.ok(ResponseData.of(true, "Zona registrada"));
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> actualizarZona(@Valid @RequestBody ActualizarZona zona) {
        zonaCommandService.actualizarZona(zona);
        return ResponseEntity.ok(ResponseData.of(true, "Zona actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> eliminarZona(@PathVariable Integer id) {
        zonaCommandService.eliminarZona(id);
        return ResponseEntity.ok(ResponseData.of(true, "Zona eliminada"));


    }
}