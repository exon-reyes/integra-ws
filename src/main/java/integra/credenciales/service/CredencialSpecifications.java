package integra.credenciales.service;

import integra.credenciales.entity.CuentaEntity;
import integra.credenciales.request.FiltroCuenta;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CredencialSpecifications {

    public static Specification<CuentaEntity> conFiltro(FiltroCuenta filtro) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filtro.idUnidad() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("unidad").get("id"), filtro.idUnidad()));
            }

            if (filtro.idDepartamento() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("departamento").get("id"), filtro.idDepartamento()));
            }

            if (filtro.idTipo() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("tipo").get("id"), filtro.idTipo()));
            }
            return predicate;
        };
    }
}
