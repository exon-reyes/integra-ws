package integra.credenciales.repository;

import integra.credenciales.entity.CuentaEntity;
import integra.credenciales.query.CuentaEntityDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CuentaEntityRepositoryCustom {
    List<CuentaEntityDto> findAllWithRelations(Specification<CuentaEntity> spec);
}