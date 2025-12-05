package integra.operatividad.repository;

import integra.operatividad.entity.HorarioOperativoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnidadHorarioJpaRepository extends JpaRepository<HorarioOperativoEntity, Integer> {
    <T> List<T> findByUnidadId(Integer id, Class<T> type);

    Integer id(Integer id);
}