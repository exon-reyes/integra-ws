package integra.seguridad.rol.service;

import integra.seguridad.rol.model.Rol;
import integra.seguridad.rol.repository.RoleRepository;
import integra.seguridad.rol.repository.RolesQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObtenerRolesService {
    private final RoleRepository roleRepository;

    public List<Rol> obtenerInfoRolesBasico() {
        return roleRepository.findBy(RolesQuery.class)
                .stream()
                .map(data -> new Rol(data.id(), data.name(), data.description(), data.isDefault()))
                .toList();
    }
}