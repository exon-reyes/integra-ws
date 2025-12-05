package integra.seguridad.usuario.service;

import integra.globalexception.DataNotFoundException;
import integra.seguridad.rol.command.ActualizarPermisoEspecialCommand;
import integra.seguridad.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActualizarPermisosEspecialesService {
    private final UserRepository userRepository;

    public void actualizarPermisosEspeciales(ActualizarPermisoEspecialCommand command) {
        var user = userRepository.findById(command.getId())
                .orElseThrow(() -> new DataNotFoundException("Usuario no encontrado", "No se pudo actualizar los permisos, usuario no encontrado"));
        user.getUserPermissions().clear();
//
//        if (command.getPermisosIds() != null && !command.getPermisosIds().isEmpty()) {
//            // Mapea cada ID a una nueva instancia de Permission usando el constructor (Long id)
//            List<Permission> permisosReferencias = command.getPermisosIds()
//                    .stream()
//                    .map(Permission::new)
//                    .toList();
//            user.setUserPermissions(permisosReferencias);
//        }

    }
}
