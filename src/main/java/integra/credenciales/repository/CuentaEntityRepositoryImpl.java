package integra.credenciales.repository;

import integra.credenciales.entity.CuentaEntity;
import integra.credenciales.query.CuentaEntityDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@RequiredArgsConstructor
public class CuentaEntityRepositoryImpl implements CuentaEntityRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<CuentaEntityDto> findAllWithRelations(Specification<CuentaEntity> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CuentaEntityDto> cq = cb.createQuery(CuentaEntityDto.class);
        Root<CuentaEntity> root = cq.from(CuentaEntity.class);

        // JOIN para acceder a las relaciones
        var tipoJoin = root.join("tipo");
        var departamentoJoin = root.join("departamento");
        var unidadJoin = root.join("unidad");

        // Proyección al DTO usando construct
        cq.select(cb.construct(CuentaEntityDto.class,
                root.get("id"),
                tipoJoin.get("id"),
                tipoJoin.get("nombre"),
                departamentoJoin.get("id"),
                departamentoJoin.get("nombre"),
                unidadJoin.get("id"),
                unidadJoin.get("clave"),
                unidadJoin.get("nombreCompleto"),
                root.get("usuario"),
                root.get("clave"),
                root.get("nota"),
                root.get("creado"),
                root.get("actualizado")
        ));

        // Aplicar la Specification si existe
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, cq, cb);
            if (predicate != null) {
                cq.where(predicate);
            }
        }

        return entityManager.createQuery(cq).getResultList();
    }
}