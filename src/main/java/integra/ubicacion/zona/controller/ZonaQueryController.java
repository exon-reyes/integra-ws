package integra.ubicacion.zona.controller;

import integra.ubicacion.zona.entity.ZonaEntityDto;
import integra.ubicacion.zona.service.ZonaQueryService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("zonas")
@RequiredArgsConstructor
class ZonaQueryController {
    private final ZonaQueryService zonaQueryService;

    @GetMapping
    public ResponseEntity<ResponseData<List<ZonaEntityDto>>> obtenerZonas() {
        return ResponseEntity.ok(ResponseData.of(zonaQueryService.obtenerZonas(), "Zonas"));
    }
}