package integra.asistencia.repository;

import integra.asistencia.entity.PausaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PausaModelRepository extends JpaRepository<PausaModel, Integer> {
    List<PausaModel> findByAsistenciaId(Integer asistenciaId);

    Optional<PausaModel> findFirstByAsistencia_Empleado_IdAndFinNullOrderByInicioDesc(Integer id);

}