package integra.asistencia.service.kiosco;

import integra.asistencia.query.KioscoInfo;
import integra.globalexception.DataNotFoundException;
import integra.model.Unidad;
import integra.unidad.repository.UnidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class KioscoQueryService {
    private final UnidadRepository repository;

    public List<Unidad> obtenerUnidadesKiosco() {
        return repository.findByActivoTrue(KioscoInfo.class)
                .stream()
                .map(data -> new Unidad(data.id(), data.nombreCompleto(), data.requiereCamara(), data.codigoAutorizacionKiosco(), data.requiereCodigo(), data.versionKiosco(), data.tiempoCompensacion()))
                .toList();
    }

    public Unidad obtenerUnidadKiosco(Integer id) {
        return repository.findById(id, KioscoInfo.class)
                .map(data -> new Unidad(data.id(), data.nombreCompleto(), data.requiereCamara(), data.codigoAutorizacionKiosco(), data.requiereCodigo(), data.versionKiosco(), data.tiempoCompensacion()))
                .orElseThrow(() -> new DataNotFoundException("Sin resultado", "No se encontró el kiosco con el id: " + id));
    }
}
