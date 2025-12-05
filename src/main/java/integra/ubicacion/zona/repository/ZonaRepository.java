package integra.ubicacion.zona.repository;

import integra.ubicacion.zona.entity.ZonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ZonaRepository extends JpaRepository<ZonaEntity, Integer> {
    <T> List<T> findBy(Class<T> type);

    @Transactional
    @Modifying
    @Query("update ZonaEntity z set z.nombre = ?1, z.activo = ?2 where z.id = ?3")
    void updateNombreAndActivoById(String nombre, Boolean activo, Integer id);


    @Modifying
    @Query("DELETE FROM ZonaEntity u WHERE u.id = ?1")
    void deleteById(Integer id);
}