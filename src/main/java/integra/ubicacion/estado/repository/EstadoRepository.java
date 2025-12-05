package integra.ubicacion.estado.repository;

import integra.ubicacion.estado.entity.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<EstadoEntity, Integer> {
}