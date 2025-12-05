package integra.config.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParametroRepository extends JpaRepository<ParametrosAppEntity, Integer> {
}