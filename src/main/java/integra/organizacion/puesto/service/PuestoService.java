package integra.organizacion.puesto.service;

import integra.organizacion.puesto.repository.PuestoRepository;
import integra.organizacion.puesto.response.PuestoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PuestoService {
    private final PuestoRepository repository;

    public List<PuestoDto> obtenerPuestos() {
        return repository.findBy(PuestoDto.class);
    }
}