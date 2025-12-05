package integra.seguridad.usuario.service;

import integra.seguridad.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ActualizarEstatusService {
    private final UserRepository repository;

    public void execute(Long idUsuario, Boolean newStatus) {
        repository.updateEnabledById(newStatus, idUsuario);
    }
}
