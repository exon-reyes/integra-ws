package integra.unidad.controller;

import integra.security.Autoridades;
import integra.unidad.request.ActualizarUnidad;
import integra.unidad.request.NuevaUnidad;
import integra.unidad.service.UnidadCommandService;
import integra.utils.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("unidades")
@RequiredArgsConstructor
class UnidadCommandController {
    private final UnidadCommandService commandService;

    @PostMapping("registrar")
    @PreAuthorize(Autoridades.CREAR_UNIDAD)
    public ResponseEntity<ResponseData<Void>> registrarUnidad(@RequestBody @Valid NuevaUnidad command) {
        commandService.registrarUnidad(command);
        return ResponseEntity.ok(ResponseData.of(null, "Unidad registrada"));
    }

    @PutMapping("actualizar")
    @PreAuthorize(Autoridades.EDITAR_UNIDAD)
    public ResponseEntity<ResponseData<Void>> actualizarUnidad(@RequestBody @Valid ActualizarUnidad command) {
        commandService.actualizarUnidad(command);
        return ResponseEntity.ok(ResponseData.of(null, "Unidad actualizada"));
    }

    @PutMapping("estatus/{id}/{estatus}")
    public ResponseEntity<ResponseData<Void>> deshabilitarUnidad(@PathVariable Integer id, @PathVariable Boolean estatus) {
        commandService.actualizarEstatusUnidad(id, estatus);
        return ResponseEntity.ok(ResponseData.of(null, estatus ? "Unidad habilitada" : "Unidad deshabilitada"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseData<Void>> eliminarUnidad(@PathVariable Integer id) {
        commandService.eliminarUnidad(id);
        return ResponseEntity.ok(ResponseData.of(null, "Unidad eliminada"));
    }
}