package integra.observacion.controller;

import integra.observacion.ObservacionFilter;
import integra.observacion.ResumenObservacion;
import integra.observacion.handler.ActualizarEstatusObservacionHandler;
import integra.observacion.service.GeneralesObservacionService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("observaciones")
@RequiredArgsConstructor
class ObservacionController {
    private final GeneralesObservacionService GeneralesObservacionService;
    private final ActualizarEstatusObservacionHandler actualizarEstatusHandler;

    @GetMapping("filtro")
    public ResponseData<List<ResumenObservacion>> obtenerObservacionesPorFiltro(ObservacionFilter filtro) {
        return GeneralesObservacionService.obtenerObservacionesPorFiltro(filtro);
    }

    @PutMapping("actualizarEstatus/{id}/{estatusId}")
    public ResponseData<Void> actualizarEstatusObservacion(@PathVariable Integer id, @PathVariable Integer estatusId) {
        actualizarEstatusHandler.actualizarEstatusObservacion(id, estatusId, "admin");
        return ResponseData.of(null, "Estatus actualizado");
    }

    @GetMapping("unidad/filtro")
    public ResponseData<List<ResumenObservacion>> obtenerObservacionesDeUnidad(ObservacionFilter filtro) {
        filtro.setPrivado(false);
        return GeneralesObservacionService.obtenerObservacionesPorFiltro(filtro);
    }
}
