package integra.seguridad.rol.service;

import integra.seguridad.rol.command.RolCommand;
import integra.seguridad.rol.entity.Role;
import integra.seguridad.rol.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActualizarRolService {
    private final RoleRepository repository;

    public int actualizarNombreRol(Long id, RolCommand data) {
        var rol = new Role(id, data.getNombre(), data.getDescripcion());
        return repository.updateNameAndDescriptionById(rol.getName(), rol.getDescription(), rol.getId());
    }
}