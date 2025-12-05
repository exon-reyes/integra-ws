package integra.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import integra.model.Usuario;
import integra.seguridad.rol.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@AllArgsConstructor
@Data
public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String fullname;


    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    private List<String> roles;        // roles reales
    private List<String> permissions;  // permisos reales
    private List<String> specialPermissions; // permisos especiales (no asociados a roles)
    private List<String> originalAuthorities; // authorities originales del JWT para refresh
    private Integer tokenVersion;
    private Boolean esSupervisor;
    private Integer empleadoId;

    public static UserPrincipal crear(Usuario usuario) {
        List<String> roles = usuario.getRoles().stream().map(Rol::getNombre).toList();
        List<String> permisos = usuario.getPermisos().stream().toList();
        List<String> permisosEspeciales = usuario.getPermisosEspeciales().stream().toList();

        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
        permisos.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
        permisosEspeciales.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));

        return new UserPrincipal(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getUsuario(), usuario.getPassword(), authorities, usuario.getActivo(), roles, permisos, permisosEspeciales, null, usuario.getTokenVersion(), usuario.getEsSupervisor(), usuario.getEmpleadoId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Para JWT: usar authorities originales si existen, sino roles + permisos especiales
    public List<String> getAuthoritiesForToken() {
        if (originalAuthorities != null) {
            return originalAuthorities;
        }
        List<String> tokenAuthorities = new ArrayList<>();
        if (roles != null) tokenAuthorities.addAll(roles);
        if (specialPermissions != null) tokenAuthorities.addAll(specialPermissions);
        return tokenAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
