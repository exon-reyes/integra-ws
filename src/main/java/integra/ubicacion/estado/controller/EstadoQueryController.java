package integra.ubicacion.estado.controller;

import integra.model.Estado;
import integra.ubicacion.estado.service.EstadoQueryService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estados")
@RequiredArgsConstructor
class EstadoQueryController {
    private final EstadoQueryService estadoQueryService;

    @GetMapping
    public ResponseEntity<ResponseData<List<Estado>>> obtenerEstados() {
        return ResponseEntity.ok(ResponseData.of(estadoQueryService.obtenerEstados(), "Estados registrados"));
    }
}