package integra.unidad.validator;

import integra.globalexception.DuplicateDataException;
import integra.unidad.repository.UnidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnidadValidator implements IUnidadValidator {
    private final UnidadRepository readRepository;

    public void checkExisteClaveUnidad(String clave) {
        if (clave != null && readRepository.existsByClave(clave)) {
            throw new DuplicateDataException("Ya existe una unidad con la clave " + clave, "Clave duplicada");
        }
    }

    public void checkExisteTelefono(String telefono) {
        if (telefono != null && readRepository.existsByTelefono(telefono)) {
            throw new DuplicateDataException("Ya existe una unidad con el telefono " + telefono, "Telefono duplicado");
        }
    }

    public void checkExisteNombre(String nombre) {
        if (nombre != null && readRepository.existsByNombre(nombre)) {
            throw new DuplicateDataException("Ya existe una unidad con el nombre " + nombre, "Nombre duplicado");
        }
    }

    public void checkExisteEmail(String email) {
        if (email != null && readRepository.existsByEmail(email)) {
            throw new DuplicateDataException("Ya existe una unidad con el email " + email, "Email duplicado");
        }
    }


}
