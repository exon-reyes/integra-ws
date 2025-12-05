package integra.seguridad.usuario.repository;

import integra.seguridad.usuario.entity.TokenVersion;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenVersionRepository extends CrudRepository<TokenVersion, Integer> {
    Optional<TokenVersion> findByUsername(String username);

}