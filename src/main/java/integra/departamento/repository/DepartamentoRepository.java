package integra.departamento.repository;

import integra.departamento.entity.DepartamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartamentoRepository extends JpaRepository<DepartamentoEntity, Integer> {
    <T> List<T> findBy(Class<T> type);
}