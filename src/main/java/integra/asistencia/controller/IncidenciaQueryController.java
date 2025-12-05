package integra.asistencia.controller;

import integra.asistencia.actions.FiltroIncidenciaRequest;
import integra.asistencia.actions.ReporteAsistenciaResponse;
import integra.asistencia.facade.ObtenerReporteAsistencia;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("asistencia/incidencia")
@RequiredArgsConstructor
public class IncidenciaQueryController {

    private final ObtenerReporteAsistencia obtenerReporteAsistencia;

    @GetMapping
    public ResponseEntity<ResponseData<ReporteAsistenciaResponse>> obtenerIncidencias(FiltroIncidenciaRequest request) {
        ReporteAsistenciaResponse response = obtenerReporteAsistencia.execute(request);
        return ResponseEntity.ok(ResponseData.of(response, "Reporte de asistencia generado"));
    }

}
