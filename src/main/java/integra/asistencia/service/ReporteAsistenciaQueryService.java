package integra.asistencia.service;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.entity.PausaModel;
import integra.asistencia.model.Asistencia;
import integra.asistencia.model.EmpleadoReporte;
import integra.asistencia.model.PausaAsistencia;
import integra.asistencia.repository.AsistenciaRepository;
import integra.asistencia.specification.AsistenciaSpecification;
import integra.asistencia.util.TipoPausa;
import integra.empleado.EmpleadoEntity;
import integra.model.Empleado;
import integra.model.Puesto;
import integra.model.Unidad;
import integra.organizacion.puesto.entity.PuestoEntity;
import integra.unidad.entity.UnidadEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteAsistenciaQueryService {
    private final AsistenciaRepository asistenciaRepository;


    public List<EmpleadoReporte> getAsistencias(EmpleadoReporteCommand command) {
        Specification<AsistenciaModel> spec = AsistenciaSpecification.findByCriteriaWithFetch(command);
        var allAsistencias = asistenciaRepository.findAll(spec);

        if (allAsistencias.isEmpty()) {
            return new ArrayList<>();
        }

        Map<EmpleadoEntity, List<AsistenciaModel>> grouped = allAsistencias.stream()
                .collect(Collectors.groupingBy(AsistenciaModel::getEmpleado));

        return grouped.entrySet().stream().map(entry -> {
            EmpleadoEntity empModel = entry.getKey();
            List<Asistencia> asistencias = entry.getValue()
                    .stream()
                    .map(this::toDomainAsistencia)      // convierte AsistenciaModel → Asistencia
                    .toList();

            var empleado = toDomainEmpleado(empModel);
            var unidad = toDomainUnidad(empModel.getUnidad());
            var puesto = toDomainPuesto(empModel.getPuesto());

            LocalDate fechaDesde = command.getDesde() != null ? command.getDesde().toLocalDate() : null;
            LocalDate fechaHasta = command.getHasta() != null ? command.getHasta().toLocalDate() : null;

            return new EmpleadoReporte(empleado.getId(), empleado, unidad, puesto, asistencias, fechaDesde, fechaHasta);
        }).toList();
    }

    private Empleado toDomainEmpleado(EmpleadoEntity empleadoModel) {
        return new Empleado(empleadoModel.getId(), empleadoModel.getCodigoEmpleado(), empleadoModel.getNombre(), empleadoModel.getApellidoPaterno(), empleadoModel.getApellidoMaterno());
    }

    private Puesto toDomainPuesto(PuestoEntity puestoModel) {
        return new Puesto(puestoModel.getId(), puestoModel.getNombre());
    }

    private Unidad toDomainUnidad(UnidadEntity unidadModel) {
        return new Unidad(unidadModel.getClave(), unidadModel.getNombre());
    }

    private Asistencia toDomainAsistencia(AsistenciaModel asistenciaModel) {
        Asistencia asistencia = new Asistencia(asistenciaModel.getId());
        asistencia.setFecha(asistenciaModel.getFecha());
        asistencia.setInicioJornada(asistenciaModel.getInicioJornada());
        asistencia.setFinJornada(asistenciaModel.getFinJornada());
        asistencia.setJornadaCerrada(asistenciaModel.getJornadaCerrada());
        asistencia.setPathFotoInicio(asistenciaModel.getPathFotoInicio());
        asistencia.setPathFotoFin(asistenciaModel.getPathFotoFin());
        asistencia.setComentario(asistenciaModel.getComentario());
        asistencia.setCerradoAutomatico(asistenciaModel.getCerradoAutomatico());
        asistencia.setInconsistencia(asistenciaModel.getInconsistencia());
        asistencia.setPausas(toDomainPausas(asistenciaModel.getPausas()));
        asistencia.setTiempoCompensado(asistenciaModel.getTiempoCompensado());
        if (verificarExistenciaNocturna(asistenciaModel)) {
            asistencia.setFueAsistenciaNocturna(true);
        }
        return asistencia;
    }

    private Boolean verificarExistenciaNocturna(AsistenciaModel asistenciaModel) {
        if (asistenciaModel.getInicioJornada() != null && asistenciaModel.getFinJornada() != null) {
            // Verificar si la fecha de inicio es un día anterior a la fecha de fin (cierre)
            return asistenciaModel.getInicioJornada()
                    .toLocalDate()
                    .isBefore(asistenciaModel.getFinJornada().toLocalDate());
        }
        return false;
    }

    private List<PausaAsistencia> toDomainPausas(List<PausaModel> pausaModels) {
        return pausaModels.stream().map(pausaModel -> {
            PausaAsistencia pausa = new PausaAsistencia(pausaModel.getId());
            pausa.setTipoPausa(TipoPausa.valueOf(pausaModel.getTipo()));
            pausa.setInicio(pausaModel.getInicio());
            pausa.setFin(pausaModel.getFin());
            pausa.setPathFotoInicio(pausaModel.getPathFotoInicio());
            pausa.setPathFotoFin(pausaModel.getPathFotoFin());
            return pausa;
        }).toList();
    }
}