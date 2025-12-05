package integra.asistencia.specification;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.entity.CompensacionSalidaDepositoEntity;
import integra.asistencia.query.CompensacionReporteQuery;
import integra.empleado.EmpleadoEntity;
import integra.organizacion.puesto.entity.PuestoEntity;
import integra.unidad.entity.UnidadEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompensacionSpecification {

    public static Specification<CompensacionSalidaDepositoEntity> findByCriteriaWithFetch(final EmpleadoReporteCommand request) {
        return (root, query, cb) -> {
            // Fetch optimizado sin duplicados
            Fetch<CompensacionSalidaDepositoEntity, EmpleadoEntity> empleadoFetch = root.fetch("empleado", JoinType.LEFT);
            empleadoFetch.fetch("puesto", JoinType.LEFT);
            empleadoFetch.fetch("unidad", JoinType.LEFT);

            if (query != null) {
                query.distinct(true);
            }

            List<Predicate> predicates = buildPredicates(root, cb, request);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<CompensacionSalidaDepositoEntity> findByCriteriaProjection(final EmpleadoReporteCommand request) {
        return (root, query, cb) -> {
            Join<CompensacionSalidaDepositoEntity, EmpleadoEntity> empleadoJoin = root.join("empleado");
            Join<CompensacionSalidaDepositoEntity, UnidadEntity> unidadJoin = root.join("unidad");

            if (query != null && query.getResultType() == CompensacionReporteQuery.class) {
                query.multiselect(empleadoJoin.get("codigoEmpleado"), empleadoJoin.get("nombreCompleto"), unidadJoin.get("nombreCompleto"), root.get("fecha"), root.get("horaSalida"), root.get("horasTrabajadas"), root.get("horasFaltantes"), root.get("tiempoCompensado"));
            }

            List<Predicate> predicates = buildPredicates(root, cb, request);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static List<Predicate> buildPredicates(Root<CompensacionSalidaDepositoEntity> root, CriteriaBuilder cb, EmpleadoReporteCommand request) {
        List<Predicate> predicates = new ArrayList<>();

        Join<CompensacionSalidaDepositoEntity, EmpleadoEntity> empleadoJoin = root.join("empleado");
        Join<EmpleadoEntity, PuestoEntity> puestoJoin = empleadoJoin.join("puesto");

        // Filtros por IDs
        addEqualPredicate(predicates, cb, empleadoJoin.get("id"), request.getEmpleadoId());
        addEqualPredicate(predicates, cb, empleadoJoin.get("unidad").get("id"), request.getUnidadId());
        addEqualPredicate(predicates, cb, puestoJoin.get("id"), request.getPuestoId());

        // Filtro supervisor
        if (request.getSupervisorId() != null) {
            Join<EmpleadoEntity, UnidadEntity> unidadJoin = empleadoJoin.join("unidad");
            predicates.add(cb.equal(unidadJoin.get("supervisor").get("id"), request.getSupervisorId()));
        }

        // Filtro rango de fechas con BETWEEN
        addDateRangePredicate(predicates, cb, root.get("fecha"), request.getDesde().toLocalDate(), request.getHasta()
                .toLocalDate());

        return predicates;
    }

    /**
     * Agrega un predicado de igualdad si el valor no es null
     */
    private static <T> void addEqualPredicate(List<Predicate> predicates, CriteriaBuilder cb, Expression<T> field, T value) {
        if (value != null) {
            predicates.add(cb.equal(field, value));
        }
    }

    /**
     * Agrega filtro de rango de fechas con BETWEEN
     * Ambas fechas deben existir
     */
    private static void addDateRangePredicate(List<Predicate> predicates, CriteriaBuilder cb, Path<LocalDate> dateField, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null) {
            predicates.add(cb.between(dateField, desde, hasta));
        }
    }
}