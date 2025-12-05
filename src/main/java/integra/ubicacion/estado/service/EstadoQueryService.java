package integra.ubicacion.estado.service;

import integra.model.Estado;
import integra.ubicacion.estado.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EstadoQueryService {
    private final EstadoRepository repository;

    @Transactional(readOnly = true)
    public List<Estado> obtenerEstados() {
        return repository.findAll().stream().map(x -> new Estado(x.getId(), x.getCodigo(), x.getNombre())).toList();
    }
}