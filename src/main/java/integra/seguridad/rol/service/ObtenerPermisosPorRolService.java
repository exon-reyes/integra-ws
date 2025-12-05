package integra.seguridad.rol.service;

import integra.model.Permiso;
import integra.seguridad.rol.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObtenerPermisosPorRolService {
    private final RoleRepository roleRepository;

    public List<Permiso> obtenerPermisosPorRol(Long id) {
        return roleRepository.findById(id)
                .map(role -> role.getPermissions()
                        .stream()
                        .map(permission -> new Permiso(permission.getId(), permission.getName()))
                        .toList())
                .orElse(List.of());
    }
}