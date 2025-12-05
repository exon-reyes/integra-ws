package integra.credenciales.repository;

import integra.credenciales.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CuentaEntityRepository extends JpaRepository<CuentaEntity, Integer>, JpaSpecificationExecutor<CuentaEntity>, CuentaEntityRepositoryCustom {
    @Modifying
    @Transactional
    @Query("DELETE FROM CuentaEntity c WHERE c.id = :id")
    void deleteById(@Param("id") Integer id);
}