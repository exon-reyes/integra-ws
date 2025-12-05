package integra.seguridad.rol.repository;

import integra.seguridad.rol.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @EntityGraph(attributePaths = {"permissions"})
    @Override
    List<Role> findAll();

    Optional<Role> findByName(String name);

    <T> List<T> findBy(Class<T> type);

    @Transactional
    @Modifying
    @Query("update Role r set r.name = ?1, r.description = ?2 where r.id = ?3")
    int updateNameAndDescriptionById(String name, String description, Long id);


}