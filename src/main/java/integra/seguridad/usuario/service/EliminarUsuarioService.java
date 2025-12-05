package integra.seguridad.usuario.service;

import integra.config.db.SystemIdProvider;
import integra.globalexception.BusinessRuleException;
import integra.seguridad.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class EliminarUsuarioService {
    private final UserRepository repository;
    private final SystemIdProvider systemIdProvider;

    public void execute(Long id) {
        if (id.equals(systemIdProvider.getIdUsuarioAdmin())) {
            System.out.println("Usuario admin");
            throw new BusinessRuleException("No se puede eliminar el usuario administrador");
        }

        repository.deleteById(id);
    }
}
