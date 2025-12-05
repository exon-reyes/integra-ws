package integra.ubicacion.zona.service;

import integra.ubicacion.zona.entity.ZonaEntity;
import integra.ubicacion.zona.repository.ZonaRepository;
import integra.ubicacion.zona.request.ActualizarZona;
import integra.ubicacion.zona.request.NuevaZona;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZonaCommandService {
    private final ZonaRepository repository;

    @Transactional
    public void registrarZona(NuevaZona nuevaZona) {
        ZonaEntity entity = new ZonaEntity(nuevaZona.getNombre());
        repository.save(entity);
    }

    @Transactional
    public void actualizarZona(ActualizarZona data) {
        repository.updateNombreAndActivoById(data.getNombre(), data.getActivo(), data.getId());
    }

    @Transactional
    public void eliminarZona(Integer id) {
        repository.deleteById(id);
        log.info("Zona eliminada: {}", id);

    }
}