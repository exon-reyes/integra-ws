package integra.seguridad.usuario.service;

import integra.globalexception.DataNotFoundException;
import integra.model.Usuario;
import integra.seguridad.rol.model.Rol;
import integra.seguridad.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ObtenerPrivilegiosUsuario {

    private final UserRepository userRepository;

    public Usuario execute(Long id) {
        var dbUser = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No se pudo procesar", "Usuario no encontrado, verifique su información"));
        Usuario usuario = new Usuario();
        usuario.setId(dbUser.getId());
        usuario.setNombre(dbUser.getUsername());
        usuario.setEmail(dbUser.getEmail());
        usuario.setActivo(dbUser.getEnabled());
        Set<Rol> roles = new HashSet<>();
        dbUser.getUserRoles().forEach(value -> {
            Rol rol = new Rol(value.getRole().getId(), value.getRole().getName(), value.getRole().getDescription());
            rol.setPermisosList(value.getRole().getPermissionsList());
            roles.add(rol);
        });
        usuario.setRoles(roles);
        usuario.setPermisos(dbUser.getPermissionNameList());
        return usuario;
    }
}
