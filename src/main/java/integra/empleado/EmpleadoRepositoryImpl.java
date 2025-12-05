package integra.empleado;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmpleadoRepositoryImpl implements EmpleadoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<InfoBasicaEmpleado> findWithFilters(EmpleadoFiltros filtros) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InfoBasicaEmpleado> query = cb.createQuery(InfoBasicaEmpleado.class);
        Root<EmpleadoEntity> root = query.from(EmpleadoEntity.class);

        // Proyección DTO
        query.select(cb.construct(InfoBasicaEmpleado.class, root.get("id"), root.get("codigoEmpleado"),
                root.get("puesto")
                        .get("nombre"),
                root.get("estatus"), root.get("nombreCompleto"), root.get("unidad")
                        .get("nombreCompleto"),
                root.get("fechaAlta"), root.get("fechaBaja")));

        // Filtros dinámicos
        List<Predicate> predicates = new ArrayList<>();

        if (filtros != null) {
            if (filtros.getNombreCompleto() != null) {
                predicates.add(cb.like(cb.lower(root.get("nombreCompleto")), "%" + filtros.getNombreCompleto()
                        .toLowerCase() + "%"));
            }

            if (filtros.getClave() != null) {
                predicates.add(cb.like(root.get("codigoEmpleado"), "%" + filtros.getClave() + "%"));
            }
            if (filtros.getPuesto() != null) {
                predicates.add(cb.like(cb.lower(root.get("puesto").get("nombre")), "%" + filtros.getPuesto()
                        .toLowerCase() + "%"));
            }

            if (filtros.getUnidad() != null) {
                predicates.add(cb.like(cb.lower(root.get("unidad").get("nombreCompleto")), "%" + filtros.getUnidad()
                        .toLowerCase() + "%"));
            }

            if (filtros.getUnidadId() != null) {
                predicates.add(cb.equal(root.get("unidad").get("id"), filtros.getUnidadId()));
            }

            if (filtros.getEstado() != null) {
                predicates.add(cb.equal(root.get("estatus"), filtros.getEstado()));
            }

            if (filtros.getIdSupervisor() != null) {
                predicates.add(cb.equal(root.get("unidad").get("supervisor").get("id"), filtros.getIdSupervisor()));
            }

            if (filtros.getIdZona() != null) {
                predicates.add(cb.equal(root.get("unidad").get("zona").get("id"), filtros.getIdZona()));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}