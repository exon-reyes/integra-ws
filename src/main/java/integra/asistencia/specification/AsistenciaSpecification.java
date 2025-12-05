package integra.asistencia.specification;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.entity.AsistenciaModel;
import integra.empleado.EmpleadoEntity;
import integra.organizacion.puesto.entity.PuestoEntity;
import integra.unidad.entity.UnidadEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaSpecification {

    public static Specification<AsistenciaModel> findByCriteriaWithFetch(final EmpleadoReporteCommand request) {
        return (root, query, cb) -> {
            root.fetch("empleado", JoinType.LEFT).fetch("puesto", JoinType.LEFT);
            root.fetch("empleado", JoinType.LEFT).fetch("unidad", JoinType.LEFT);
            root.fetch("empleado", JoinType.LEFT).fetch("unidad", JoinType.LEFT);
            root.fetch("pausas", JoinType.LEFT);

            List<Predicate> predicates = buildPredicates(root, cb, request);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static List<Predicate> buildPredicates(Root<AsistenciaModel> root, CriteriaBuilder cb, EmpleadoReporteCommand request) {
        List<Predicate> predicates = new ArrayList<>();

        Join<AsistenciaModel, EmpleadoEntity> empleadoJoin = root.join("empleado");
        Join<EmpleadoEntity, PuestoEntity> puestoJoin = empleadoJoin.join("puesto");

        if (request.getEmpleadoId() != null) {
            predicates.add(cb.equal(empleadoJoin.get("id"), request.getEmpleadoId()));
        }

        if (request.getUnidadId() != null) {
            predicates.add(cb.equal(empleadoJoin.get("unidad").get("id"), request.getUnidadId()));
        }

        if (request.getPuestoId() != null) {
            predicates.add(cb.equal(puestoJoin.get("id"), request.getPuestoId()));
        }

        if (request.getSupervisorId() != null) {
            Join<EmpleadoEntity, UnidadEntity> unidadJoin = empleadoJoin.join("unidad");
            predicates.add(cb.equal(unidadJoin.get("supervisor").get("id"), request.getSupervisorId()));
        }

        if (request.getZonaId() != null) {
            Join<EmpleadoEntity, UnidadEntity> unidadJoin = empleadoJoin.join("unidad");
            predicates.add(cb.equal(unidadJoin.get("zona").get("id"), request.getZonaId()));
        }

        if (request.getDesde() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("inicioJornada"), request.getDesde()));
        }

        if (request.getHasta() != null) {
            LocalDateTime endOfDay = request.getHasta().toLocalDate().atTime(23, 59, 59);
            predicates.add(cb.lessThanOrEqualTo(root.get("inicioJornada"), endOfDay));
        }

        return predicates;
    }
}