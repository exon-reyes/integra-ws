package integra.credenciales.repository;

import integra.credenciales.entity.TipoCuentaEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TipoCuentaRepository extends CrudRepository<TipoCuentaEntity, Integer> {
    <T> List<T> findBy(Class<T> type);
}