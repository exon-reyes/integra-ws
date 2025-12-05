package integra.asistencia.controller;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.actions.EmpleadoReporteRequest;
import integra.asistencia.actions.FiltroIncidencia;
import integra.asistencia.entity.Incidencia;
import integra.asistencia.facade.ReporteFacade;
import integra.asistencia.model.EmpleadoReporte;
import integra.asistencia.service.WorkTimeImageService;
import integra.utils.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static integra.asistencia.factory.EmpleadoFactory.mapRequestToCommand;

/**
 * Controlador REST para la gestión de reportes de asistencia de empleados.
 * Proporciona endpoints para obtener reportes consolidados en formato JSON y
 * reportes detallados en formato Excel.
 */
@RestController
@RequestMapping("asistencia/reporte")
@RequiredArgsConstructor
@Slf4j
public class ReporteQueryController {

    private final ReporteFacade reporteFacade;
    private final WorkTimeImageService imageService;

    /**
     * Obtiene un reporte consolidado de asistencias de empleados en formato JSON.
     *
     * @param request Objeto con los parámetros de filtrado para generar el reporte
     * @return ResponseEntity con los datos del reporte de asistencias de empleados
     */
    @GetMapping("/asistencias")
    public ResponseEntity<ResponseData<List<EmpleadoReporte>>> obtenerReporteJson(@Valid EmpleadoReporteRequest request) {
        EmpleadoReporteCommand command = mapRequestToCommand(request);
        return ResponseEntity.ok(ResponseData.of(reporteFacade.obtenerAsistencia(command), "Asistencia por empleados"));
    }


    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        log.info("Consultando la imagen {}", filename);
        Resource image = imageService.getResizedImg(filename, 300, 300);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }

    /**
     * Genera y descarga un reporte detallado de asistencias con pausas anidadas en formato Excel.
     *
     * @param request Objeto con los parámetros de filtrado para generar el reporte
     * @return ResponseEntity con el archivo Excel del reporte detallado de asistencias
     */
    @GetMapping("/asistencias/detallado/excel")
    public ResponseEntity<byte[]> obtenerReporteDetalladoExcel(@Valid EmpleadoReporteRequest request) {
        EmpleadoReporteCommand command = mapRequestToCommand(request);
        byte[] excelBytes = reporteFacade.obtenerReporteAsistenciaExcel(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "reporte-asistencias-detallado.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }

    @GetMapping("/inconsistencias")
    public ResponseEntity<ResponseData<List<Incidencia>>> obtenerInconsistenciasAsistencia(FiltroIncidencia filtro) {
        return ResponseEntity.ok(ResponseData.of(reporteFacade.obtenerInconsistenciasAsistencia(filtro.getFechaInicio(), filtro.getFechaFin(), filtro.getEmpleadoId()), "Inconsistencias de asistencia"));
    }


}