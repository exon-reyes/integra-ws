package integra.security.service;

import integra.config.db.SystemIdProvider;
import integra.empleado.EmpleadoEntity;
import integra.empleado.EmpleadoRepository;
import integra.globalexception.LoginException;
import integra.model.Usuario;
import integra.security.UserPrincipal;
import integra.seguridad.rol.model.Rol;
import integra.seguridad.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EmpleadoRepository empleadoRepository;
    private final SystemIdProvider systemIdProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new LoginException("Acceso denegado", "Verifique sus credenciales de acceso"));
        Optional<EmpleadoEntity> empleado = Optional.empty();
        if (user.getEmpleadoId() != null) {
            empleado = empleadoRepository.findById(user.getEmpleadoId());
        }

        Usuario usuario = new Usuario(user.getId(), user.getUsername(), user.getPassword(), user.getFullname(), user.getEmail(), user.getEnabled());
        // Obtener permisos de roles
        Set<String> permisosDeRoles = new HashSet<>();

        Set<Rol> roles = user.getUserRoles().stream().map(userRole -> {
            var rol = userRole.getRole();
            permisosDeRoles.addAll(rol.getPermissionsList());
            return new Rol(rol.getName());
        }).collect(Collectors.toSet());

        // Todos los permisos directos del usuario
        Set<String> permisosDirectos = new HashSet<>(user.getPermissionNameList());

        // Permisos especiales = permisos directos - permisos que ya vienen de roles
        Set<String> permisosEspeciales = new HashSet<>(permisosDirectos);
        permisosEspeciales.removeAll(permisosDeRoles);

        // Todos los permisos para UI
        Set<String> todosLosPermisos = new HashSet<>(permisosDeRoles);
        todosLosPermisos.addAll(permisosDirectos);

        usuario.setRoles(roles);
        empleado.ifPresent(empleadoEntity -> {
            usuario.setEmpleadoId(empleadoEntity.getId());
            usuario.setEsSupervisor(empleadoEntity.getPuesto()
                    .getId()
                    .equals(systemIdProvider.getIdPuestoSupervisor()));
        });
        usuario.setUsuario(user.getFullname());
        usuario.setPermisos(todosLosPermisos); // todos para UI
        usuario.setPermisosEspeciales(permisosEspeciales); // solo especiales para JWT
        usuario.setAuthorities(todosLosPermisos);

        return UserPrincipal.crear(usuario);
    }

}