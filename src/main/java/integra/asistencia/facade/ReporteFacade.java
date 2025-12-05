package integra.asistencia.facade;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.entity.Incidencia;
import integra.asistencia.model.EmpleadoReporte;
import integra.asistencia.repository.KioscoUnidadIncidenciaRepository;
import integra.asistencia.service.ExportarAsistenciaExcelService;
import integra.asistencia.service.ReporteAsistenciaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteFacade {
    private final ReporteAsistenciaQueryService asistenciaService;
    private final ExportarAsistenciaExcelService exportarAsistenciaExcelService;
    private final KioscoUnidadIncidenciaRepository incidenciaRepository;

    public List<EmpleadoReporte> obtenerAsistencia(EmpleadoReporteCommand cmd) {
        return asistenciaService.getAsistencias(cmd);
    }

    /**
     * Genera un reporte de asistencias en Excel usando EmpleadoReporte como datasource.
     * Este método utiliza la estructura optimizada de EmpleadoReporte.
     */
    public byte[] obtenerReporteAsistenciaExcel(EmpleadoReporteCommand command) {
        List<EmpleadoReporte> datosReporte = asistenciaService.getAsistencias(command);
        try {
            return exportarAsistenciaExcelService.generarReporteAsistenciaExcel(datosReporte, command);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el reporte de asistencias en Excel", e);
        }
    }

    public List<Incidencia> obtenerInconsistenciasAsistencia(LocalDateTime desde, LocalDateTime hasta, Integer empleadoId) {
        return incidenciaRepository.findByFechaBetweenAndEmpleadoId(desde, hasta, empleadoId);
    }
}