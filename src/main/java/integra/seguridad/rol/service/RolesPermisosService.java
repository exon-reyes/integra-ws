package integra.seguridad.rol.service;

import integra.model.Permiso;
import integra.seguridad.rol.model.Rol;
import integra.seguridad.rol.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RolesPermisosService {
    private final RoleRepository repository;

    @Cacheable(value = "rolesPermisos", unless = "#result == null")
    public List<Rol> obtenerRolesConPermisos() {
        return repository.findAll()
                .stream()
                .map(role -> new Rol(role.getId(), role.getName(), role.getDescription(), role.getPermissions()
                        .stream()
                        .map(permission -> new Permiso(permission.getId(), permission.getName()))
                        .collect(Collectors.toSet())))
                .toList();
    }

    @CacheEvict(value = "rolesPermisos", allEntries = true)
    public void limpiarCacheRolesPermisos() {
        // Solo limpia cache, no es necesario código adicional
    }
}
