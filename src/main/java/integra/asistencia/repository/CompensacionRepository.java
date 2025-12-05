package integra.asistencia.repository;

import integra.asistencia.entity.CompensacionSalidaDepositoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompensacionRepository extends JpaRepository<CompensacionSalidaDepositoEntity, Integer>, JpaSpecificationExecutor<CompensacionSalidaDepositoEntity> {
}