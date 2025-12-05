package integra.ubicacion.zona.service;

import integra.ubicacion.zona.entity.ZonaEntityDto;
import integra.ubicacion.zona.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZonaQueryService {
    private final ZonaRepository repository;

    @Transactional(readOnly = true)
    public List<ZonaEntityDto> obtenerZonas() {
        return repository.findBy(ZonaEntityDto.class);
    }
}