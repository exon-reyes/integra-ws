package integra.seguridad.usuario.service;

import integra.seguridad.rol.entity.Role;
import integra.seguridad.rol.entity.UserRole;
import integra.seguridad.rol.repository.RoleRepository;
import integra.seguridad.rol.repository.UserRoleRepository;
import integra.seguridad.usuario.actions.CrearUsuarioCommand;
import integra.seguridad.usuario.entity.User;
import integra.seguridad.usuario.exception.CreateUserException;
import integra.seguridad.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CrearUsuarioService {
    private final UserRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public void crearUsuario(CrearUsuarioCommand command) {
        if (usuarioRepository.existsByUsername(command.getUsername())) {
            throw new CreateUserException("El nombre de usuario ya existe");
        }
        if (command.getEmail() != null && !command.getEmail()
                .isEmpty() && usuarioRepository.existsByEmail(command.getEmail())) {
            throw new CreateUserException("El email ya está registrado");
        }

        var usuarioNuevo = new User();
        usuarioNuevo.setUsername(command.getUsername());
        usuarioNuevo.setFullname(command.getFullname());
        usuarioNuevo.setEmail(command.getEmail());
        usuarioNuevo.setPassword(passwordEncoder.encode(command.getPassword()));


        User savedUser = usuarioRepository.save(usuarioNuevo);

        // Asignar roles después de guardar el usuario
        if (command.getRoles() != null && !command.getRoles().isEmpty()) {
            command.getRoles().forEach(roleId -> {
                UserRole userRole = new UserRole();
                Role role = new Role();
                role.setId(roleId);
                userRole.setRole(role);
                userRole.setUser(savedUser);
                userRoleRepository.save(userRole);
            });
        }
    }
}