package integra.asistencia.repository;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.query.AsistenciaProyeccion;
import integra.empleado.EmpleadoEntity;
import integra.organizacion.puesto.entity.PuestoEntity;
import integra.ubicacion.zona.entity.ZonaEntity;
import integra.unidad.entity.UnidadEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AsistenciaRepositoryImpl implements AsistenciaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AsistenciaProyeccion> findProyeccionByCriteria(EmpleadoReporteCommand request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AsistenciaProyeccion> query = cb.createQuery(AsistenciaProyeccion.class);
        Root<AsistenciaModel> root = query.from(AsistenciaModel.class);

        Join<AsistenciaModel, EmpleadoEntity> empleadoJoin = root.join("empleado", JoinType.INNER);
        Join<EmpleadoEntity, PuestoEntity> puestoJoin = empleadoJoin.join("puesto", JoinType.LEFT);
        Join<EmpleadoEntity, UnidadEntity> unidadJoin = empleadoJoin.join("unidad", JoinType.LEFT);
        Join<UnidadEntity, ZonaEntity> zonaJoin = unidadJoin.join("zona", JoinType.LEFT);

        // Proyección
        query.select(cb.construct(AsistenciaProyeccion.class,
                empleadoJoin.get("id"),
                empleadoJoin.get("codigoEmpleado"),
                empleadoJoin.get("nombreCompleto"),
                puestoJoin.get("nombre"),
                unidadJoin.get("nombreCompleto"),
                zonaJoin.get("nombre"),
                unidadJoin.get("supervisor").get("nombreCompleto"),
                root.get("fecha"),
                cb.isNotNull(root.get("inicioJornada"))));

        // Predicados (reutilizando lógica similar a Specification pero adaptada a los
        // Joins ya creados)
        List<Predicate> predicates = new ArrayList<>();

        if (request.getEmpleadoId() != null) {
            predicates.add(cb.equal(empleadoJoin.get("id"), request.getEmpleadoId()));
        }

        if (request.getUnidadId() != null) {
            predicates.add(cb.equal(unidadJoin.get("id"), request.getUnidadId()));
        }

        if (request.getPuestoId() != null) {
            predicates.add(cb.equal(puestoJoin.get("id"), request.getPuestoId()));
        }

        if (request.getSupervisorId() != null) {
            predicates.add(cb.equal(unidadJoin.get("supervisor").get("id"), request.getSupervisorId()));
        }

        if (request.getZonaId() != null) {
            predicates.add(cb.equal(zonaJoin.get("id"), request.getZonaId()));
        }

        if (request.getDesde() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("inicioJornada"), request.getDesde()));
        }

        if (request.getHasta() != null) {
            LocalDateTime endOfDay = request.getHasta().toLocalDate().atTime(23, 59, 59);
            predicates.add(cb.lessThanOrEqualTo(root.get("inicioJornada"), endOfDay));
        }

        // Asegurar que solo traemos registros válidos si es necesario, o manejar
        // filtros adicionales
        // En este caso, el usuario pidió "si almenos tienen entrada contabilizar como
        // día trabajado"
        // La proyección ya calcula el booleano 'asistencia' basado en inicioJornada !=
        // null.
        // Pero el filtro de fechas debe aplicarse a la fecha de asistencia o
        // inicioJornada.
        // El request tiene 'desde' y 'hasta' como LocalDateTime, pero AsistenciaModel
        // tiene 'fecha' (LocalDate) y 'inicioJornada' (LocalDateTime).
        // Ajustamos el filtro de fechas para usar 'fecha' si 'inicioJornada' es nulo?
        // No, la especificación original usaba 'inicioJornada'. Mantendré eso.

        // Sin embargo, si inicioJornada es nulo, no saldrá en el rango si filtramos por
        // inicioJornada.
        // El requerimiento dice "obtener la asistencia... en ese periodo".
        // Si no tiene asistencia, no tiene inicioJornada.
        // Pero aquí estamos consultando la tabla de ASISTENCIA. Si no hay registro, no
        // hay fila.
        // Si hay registro pero inicioJornada es null (ej. falta injustificada creada
        // pero vacía?), entonces:
        // Si filtramos por inicioJornada, esos registros se pierden.
        // Deberíamos filtrar por 'fecha' del modelo AsistenciaModel para el rango.

        if (request.getDesde() != null && request.getHasta() != null) {
            predicates.add(
                    cb.between(root.get("fecha"), request.getDesde().toLocalDate(), request.getHasta().toLocalDate()));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}
