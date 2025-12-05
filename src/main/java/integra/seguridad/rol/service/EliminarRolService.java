package integra.seguridad.rol.service;

import integra.seguridad.rol.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EliminarRolService {
    private final RoleRepository repository;

    public void eliminarRol(Long id) {
        repository.deleteById(id);
    }
}