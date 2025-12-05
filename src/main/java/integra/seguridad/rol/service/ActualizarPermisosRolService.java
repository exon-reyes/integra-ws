package integra.seguridad.rol.service;

import integra.seguridad.rol.command.ActualizarPermisosCommand;
import integra.seguridad.rol.entity.Permission;
import integra.seguridad.rol.entity.Role;
import integra.seguridad.rol.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActualizarPermisosRolService {
    private final RoleRepository repository;
    private final InvalidarTokensRolService invalidarTokensService;
    private final RolesPermisosService rolesPermisosService;

    public void actualizarPermisosRol(ActualizarPermisosCommand command) {
        // 1. Buscar y validar el Role
        Role role = repository.findById(command.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // 2. Limpiar permisos actuales
        role.getPermissions().clear();

        // 3. Crear referencias de Permission usando el nuevo constructor
        if (command.getPermisosIds() != null && !command.getPermisosIds().isEmpty()) {
            // Mapea cada ID a una nueva instancia de Permission usando el constructor (Long id)
            List<Permission> permisosReferencias = command.getPermisosIds().stream().map(Permission::new).toList();
            role.getPermissions().addAll(permisosReferencias);
        }

        // 4. Incrementar versión para invalidar tokens existentes
        role.setVersion(role.getVersion() + 1);

        // 5. Persistir los cambios (JPA usará solo los IDs en la tabla JOIN)
        repository.save(role);

        // 6. Invalidar tokens de usuarios con este rol
        invalidarTokensService.invalidarTokensPorRol(command.getRolId());

        // 7. Limpiar caché de roles y permisos
        rolesPermisosService.limpiarCacheRolesPermisos();
    }
}