package integra.asistencia.controller;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.actions.EmpleadoReporteRequest;
import integra.asistencia.query.CompensacionReporteQuery;
import integra.asistencia.service.compensacion.CompensacionQueryService;
import integra.security.Autoridades;
import integra.utils.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static integra.asistencia.factory.EmpleadoFactory.mapRequestToCommand;

@RequiredArgsConstructor
@RequestMapping("opentime/compensaciones")
@RestController
public class CompensacionQueryController {
    private final CompensacionQueryService service;

    @GetMapping
    @PreAuthorize(Autoridades.VER_COMPENSACIONES_APLICADAS)
    public ResponseEntity<ResponseData<List<CompensacionReporteQuery>>> obtenerCompensaciones(@Valid EmpleadoReporteRequest request) {
        EmpleadoReporteCommand command = mapRequestToCommand(request);
        return ResponseEntity.ok(ResponseData.of(service.obtenerInforme(command), "Compensaciones"));
    }

    @GetMapping("/excel")
    @PreAuthorize(Autoridades.EXPORTAR_COMPENSACIONES)
    public ResponseEntity<byte[]> obtenerCompensacionesExcel(@Valid EmpleadoReporteRequest request) {
        EmpleadoReporteCommand command = mapRequestToCommand(request);
        byte[] excelBytes = service.obtenerCompensacionesExcel(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "compensaciones.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }
}
