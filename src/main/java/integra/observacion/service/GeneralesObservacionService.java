package integra.observacion.service;

import integra.observacion.ObservacionFilter;
import integra.observacion.ResumenObservacion;
import integra.observacion.entity.Observacion;
import integra.observacion.entity.ObservacionColaboracion;
import integra.utils.ResponseData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralesObservacionService {
    @PersistenceContext
    private EntityManager entityManager;

    public ResponseData<List<ResumenObservacion>> obtenerObservacionesPorFiltro(ObservacionFilter filtro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResumenObservacion> cq = cb.createQuery(ResumenObservacion.class);
        Root<Observacion> observacion = cq.from(Observacion.class);

        // Joins necesarios
        Join<Object, Object> tipoObservacionJoin = observacion.join("tipoObservacion", JoinType.INNER);
        Join<Object, Object> estatusJoin = observacion.join("estatus", JoinType.INNER);
        Join<Object, Object> unidadJoin = observacion.join("unidad", JoinType.INNER);
        Join<Object, Object> departamentoOrigenJoin = observacion.join("departamentoOrigen", JoinType.LEFT);
        Join<Object, Object> departamentoResponsableJoin = observacion.join("departamentoResponsable", JoinType.INNER);
        Join<Object, Object> creadoPorJoin = observacion.join("creadoPor", JoinType.LEFT);

        // Constructor para el DTO de respuesta
        cq.select(cb.construct(
                ResumenObservacion.class,
                observacion.get("id"),
                observacion.get("folioObservacion"),
                unidadJoin.get("id"),
                unidadJoin.get("nombreCompleto"),
                tipoObservacionJoin.get("nombre"),
                estatusJoin.get("nombre"),
                estatusJoin.get("colorBg"),
                departamentoOrigenJoin.get("nombre"),
                observacion.get("titulo"),
                observacion.get("prioridad"),
                observacion.get("requiereSeguimiento"),
                observacion.get("creado"),
                observacion.get("modificado")
        ));

        // Construir predicados de filtrado
        List<Predicate> predicates = buildPredicates(cb, observacion, tipoObservacionJoin, filtro);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(observacion.get("creado")));

        // Paginación
        int pagina = filtro.getPagina() != null ? filtro.getPagina() : 0;
        int filas = filtro.getFilas() != null ? filtro.getFilas() : 10;

        List<ResumenObservacion> resultList = entityManager.createQuery(cq)
                .setFirstResult(pagina * filas)
                .setMaxResults(filas)
                .getResultList();

        Long totalResults = countByFiltro(filtro);
        Page<ResumenObservacion> page = new PageImpl<>(resultList, PageRequest.of(pagina, filas), totalResults);

        return ResponseData.paginated(page.getContent(), pagina, page.getNumberOfElements(),
                page.getTotalElements(), page.getTotalPages());
    }

    private Long countByFiltro(ObservacionFilter filtro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Observacion> observacion = cq.from(Observacion.class);

        List<Predicate> predicates = buildPredicates(cb, observacion,
                observacion.join("tipoObservacion", JoinType.INNER), filtro);

        cq.select(cb.count(observacion));
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getSingleResult();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb,
                                            Root<Observacion> observacion,
                                            Join<Object, Object> tipoObservacionJoin,
                                            ObservacionFilter filtro) {
        List<Predicate> predicates = new ArrayList<>();

        // Lógica de visibilidad principal: se prioriza la colaboración
        if (filtro.getDepartamentoColaboradorId() != null) {
            // Caso 1: Traer solo observaciones de colaboración
            // Nota: Necesitarás tener la relación de colaboraciones en tu entidad Observacion
            // Si no la tienes, puedes hacer una subconsulta
            Subquery<Integer> colaboracionSubquery = cb.createQuery().subquery(Integer.class);
            Root<ObservacionColaboracion> colaboracionRoot = colaboracionSubquery.from(ObservacionColaboracion.class);
            colaboracionSubquery.select(colaboracionRoot.get("observacion").get("id"))
                    .where(cb.and(
                            cb.equal(colaboracionRoot.get("observacion").get("id"), observacion.get("id")),
                            cb.equal(colaboracionRoot.get("departamento")
                                    .get("id"), filtro.getDepartamentoColaboradorId()),
                            cb.equal(colaboracionRoot.get("activa"), true)
                    ));
            predicates.add(cb.exists(colaboracionSubquery));

        } else if (filtro.getDepartamentoResponsableId() != null) {
            // Caso 2: Traer solo observaciones donde el departamento es el responsable
            predicates.add(cb.equal(observacion.get("departamentoResponsable").get("id"),
                    filtro.getDepartamentoResponsableId()));
        }

        if (filtro.getUnidadId() != null) {
            predicates.add(cb.equal(observacion.get("unidad").get("id"), filtro.getUnidadId()));
        }

        if (filtro.getSupervisorId() != null) {
            predicates.add(cb.equal(observacion.get("unidad").get("supervisor").get("id"),
                    filtro.getSupervisorId()));
        }

        if (filtro.getZonaId() != null) {
            predicates.add(cb.equal(observacion.get("unidad").get("zona").get("id"), filtro.getZonaId()));
        }

        if (filtro.getTipoObservacionId() != null) {
            predicates.add(cb.equal(tipoObservacionJoin.get("id"), filtro.getTipoObservacionId()));
        }

        if (filtro.getEstatusId() != null) {
            predicates.add(cb.equal(observacion.get("estatus").get("id"), filtro.getEstatusId()));
        }

        if (filtro.getDepartamentoGeneraId() != null) {
            predicates.add(cb.equal(observacion.get("departamentoOrigen").get("id"),
                    filtro.getDepartamentoGeneraId()));
        }

        if (filtro.getUsuarioCreadorId() != null) {
            predicates.add(cb.equal(observacion.get("creadoPor").get("id"), filtro.getUsuarioCreadorId()));
        }

        if (filtro.getPrioridad() != null) {
            predicates.add(cb.equal(observacion.get("prioridad"), filtro.getPrioridad()));
        }

        if (filtro.getFolio() != null && !filtro.getFolio().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(observacion.get("folioObservacion")),
                    "%" + filtro.getFolio().toLowerCase() + "%"));
        }

        if (filtro.getCreadoDesde() != null) {
            predicates.add(cb.greaterThanOrEqualTo(observacion.get("creado"), filtro.getCreadoDesde()));
        }

        if (filtro.getCreadoHasta() != null) {
            predicates.add(cb.lessThanOrEqualTo(observacion.get("creado"), filtro.getCreadoHasta()));
        }

        return predicates;
    }
}