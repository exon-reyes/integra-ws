package integra.security.service;

import integra.model.Usuario;
import integra.seguridad.usuario.query.InfoBasicaUsuario;
import integra.seguridad.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<Usuario> obtenerInfoBasicaUsuarios() {
        return userRepository.findBy(InfoBasicaUsuario.class).stream().map(data -> {
            return new Usuario(data.id(), data.username(), data.email(), data.enabled());
        }).toList();

    }
}