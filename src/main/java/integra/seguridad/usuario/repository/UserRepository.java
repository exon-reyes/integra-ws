package integra.seguridad.usuario.repository;

import integra.seguridad.usuario.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"userPermissions.permission", "userRoles.role"})
    Optional<User> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.enabled = ?1 where u.id = ?2")
    int updateEnabledById(Boolean enabled, Long id);


    <T> List<T> findBy(Class<T> type);

    <T> Optional<T> findById(Long integer, Class<T> type);

    @Query("SELECT u.username FROM User u JOIN u.userRoles ur WHERE ur.role.id = :roleId")
    List<String> findUsernamesByRoleId(Long roleId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}