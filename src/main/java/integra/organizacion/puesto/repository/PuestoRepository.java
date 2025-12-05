package integra.organizacion.puesto.repository;

import integra.organizacion.puesto.entity.PuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuestoRepository extends JpaRepository<PuestoEntity, Integer> {
    <T> List<T> findBy(Class<T> type);
}