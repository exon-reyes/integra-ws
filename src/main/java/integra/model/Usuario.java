package integra.model;

import integra.seguridad.rol.model.Rol;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Usuario {
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private Set<Rol> roles;
    private Set<String> permisos;
    private Set<String> permisosEspeciales; // permisos no asociados a roles
    private Boolean activo;
    private String usuario;
    private Set<String> authorities;
    private Boolean esSupervisor;
    private Integer tokenVersion;
    private Integer empleadoId;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String email, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
    }

    public Usuario(Long id, String nombre, String password, String usuario, String email, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.email = email;
        this.activo = activo;
    }

    public Usuario(Long id, Set<String> permisos, Set<Rol> roles) {
        this.id = id;
        this.permisos = permisos;
        this.roles = roles;
    }

    public Usuario(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }
}
