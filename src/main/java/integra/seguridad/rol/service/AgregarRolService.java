package integra.seguridad.rol.service;

import integra.seguridad.rol.command.RolCommand;
import integra.seguridad.rol.entity.Permission;
import integra.seguridad.rol.entity.Role;
import integra.seguridad.rol.model.Rol;
import integra.seguridad.rol.repository.PermissionRepository;
import integra.seguridad.rol.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AgregarRolService {
    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;

    public Rol agregarRol(RolCommand command) {
        var nuevoRol = new Role();
        nuevoRol.setName(command.getNombre());
        nuevoRol.setDescription(command.getDescripcion());
        if (command.getIdPermisos() != null && !command.getIdPermisos().isEmpty()) {
            for (var idPermiso : command.getIdPermisos()) {
                nuevoRol.getPermissions().add(new Permission(idPermiso));
            }
        }
        var result = repository.save(nuevoRol);
        return new Rol(result.getId(), result.getName(), result.getDescription());
    }
}