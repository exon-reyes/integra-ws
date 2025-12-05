package integra.asistencia.facade;

import integra.asistencia.actions.EmpleadoAsistencia;
import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.actions.FiltroIncidenciaRequest;
import integra.asistencia.actions.ReporteAsistenciaResponse;
import integra.asistencia.query.AsistenciaProyeccion;
import integra.asistencia.repository.AsistenciaRepository;
import integra.empleado.EmpleadoFiltros;
import integra.empleado.EmpleadoRepository;
import integra.empleado.InfoBasicaEmpleado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObtenerReporteAsistencia {

    private final EmpleadoRepository empleadoRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Transactional(readOnly = true)
    public ReporteAsistenciaResponse execute(FiltroIncidenciaRequest request) {
        // 1. Obtener empleados según filtros (para tener el universo completo de
        // empleados)
        EmpleadoFiltros filtros = new EmpleadoFiltros();
        filtros.setIdSupervisor(request.getSupervisorId());
        filtros.setIdZona(request.getZonaId());
        filtros.setUnidadId(request.getUnidadId());

        List<InfoBasicaEmpleado> empleados = empleadoRepository.findWithFilters(filtros);

        if (request.getEmpleadoId() != null) {
            empleados = empleados.stream()
                    .filter(e -> e.id().equals(request.getEmpleadoId()))
                    .toList();
        }

        if (empleados.isEmpty()) {
            return new ReporteAsistenciaResponse(List.of(), List.of());
        }

        // 2. Definir rango de fechas
        LocalDate inicio = request.getFechaInicio().toLocalDate();
        LocalDate fin = request.getFechaFin().toLocalDate();
        List<LocalDate> rangoFechas = inicio.datesUntil(fin.plusDays(1)).toList();

        // 3. Obtener asistencias usando Proyección (optimizado)
        EmpleadoReporteCommand command = new EmpleadoReporteCommand();
        command.setEmpleadoId(request.getEmpleadoId());
        command.setUnidadId(request.getUnidadId());
        command.setZonaId(request.getZonaId());
        command.setSupervisorId(request.getSupervisorId());
        command.setDesde(request.getFechaInicio());
        command.setHasta(request.getFechaFin());

        List<AsistenciaProyeccion> asistencias = asistenciaRepository.findProyeccionByCriteria(command);

        // 4. Construir respuesta
        // Map<EmpleadoID, Map<Fecha, AsistenciaProyeccion>>
        Map<Integer, Map<LocalDate, AsistenciaProyeccion>> asistenciaMap = asistencias.stream()
                .collect(Collectors.groupingBy(
                        AsistenciaProyeccion::empleadoId,
                        Collectors.toMap(AsistenciaProyeccion::fecha, a -> a, (a1, a2) -> a1) // Si hay duplicados,
                        // tomar primero
                ));

        List<EmpleadoAsistencia> reporteEmpleados = new ArrayList<>();

        for (InfoBasicaEmpleado emp : empleados) {
            List<Integer> asistenciaDiaria = new ArrayList<>();
            Map<LocalDate, AsistenciaProyeccion> empAsistencias = asistenciaMap.getOrDefault(emp.id(), Map.of());

            // Obtener metadata del primer registro de asistencia (todos tienen los mismos
            // valores)
            AsistenciaProyeccion primeraAsistencia = empAsistencias.values().stream().findFirst().orElse(null);

            String nombreUnidad = primeraAsistencia != null ? primeraAsistencia.unidadNombreCompleto()
                    : emp.unidadNombreCompleto();
            String puesto = primeraAsistencia != null ? primeraAsistencia.puestoNombre() : emp.puestoNombre();
            String zona = primeraAsistencia != null ? primeraAsistencia.zonaNombre() : "";
            String supervisor = primeraAsistencia != null ? primeraAsistencia.supervisorNombre() : "";

            for (LocalDate fecha : rangoFechas) {
                AsistenciaProyeccion asistencia = empAsistencias.get(fecha);
                boolean asistio = asistencia != null && Boolean.TRUE.equals(asistencia.asistencia());
                asistenciaDiaria.add(asistio ? 1 : 0);
            }

            reporteEmpleados.add(new EmpleadoAsistencia(
                    emp.codigoEmpleado(),
                    emp.nombreCompleto(),
                    nombreUnidad,
                    puesto,
                    zona,
                    supervisor,
                    asistenciaDiaria));
        }

        return new ReporteAsistenciaResponse(rangoFechas, reporteEmpleados);
    }
}
