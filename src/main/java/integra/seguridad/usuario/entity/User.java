package integra.seguridad.usuario.entity;

import integra.seguridad.rol.entity.Permission;
import integra.seguridad.rol.entity.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @ColumnDefault("1")
    @Column(name = "enabled")
    private Boolean enabled;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "user")
    private Set<UserPermission> userPermissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    @Size(max = 80)
    @Column(name = "fullname", length = 80)
    private String fullname;

    @Column(name = "empleado_id")
    private Integer empleadoId;


    public Set<String> getPermissionNameList() {
        return this.userPermissions.stream()
                .map(UserPermission::getPermission)
                .map(Permission::getId)
                .collect(Collectors.toSet());
    }
}