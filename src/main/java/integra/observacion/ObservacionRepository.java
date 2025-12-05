package integra.observacion;

import integra.observacion.entity.Estatus;
import integra.observacion.entity.Observacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ObservacionRepository extends JpaRepository<Observacion, Integer> {
    <T> Optional<T> findById(Integer integer, Class<T> type);

    @Transactional
    @Modifying
    @Query("update Observacion o set o.estatus = ?1 where o.id = ?2")
    void updateEstatusById(Estatus estatus, Integer id);

    @Query("SELECT e.esFinal FROM Observacion o JOIN o.estatus e WHERE o.id = :observacionId")
    Boolean esEstatusFinal(@Param("observacionId") Integer observacionId);
}