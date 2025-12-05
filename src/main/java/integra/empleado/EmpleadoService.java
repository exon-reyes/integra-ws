package integra.empleado;

import integra.config.db.SystemIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmpleadoService {
    private final EmpleadoRepository repository;
    private final SystemIdProvider idProvider;

    public List<InfoBasicaEmpleado> obtenerInfoBasicaEmpleados() {
        return repository.findBy(InfoBasicaEmpleado.class);
    }

    public List<InfoBasicaEmpleado> obtenerEmpleadosFiltrados(EmpleadoFiltros filtros) {
        return repository.findWithFilters(filtros);
    }

    public List<InfoEmpleados> obtenerSoloEmpleados() {
        return repository.findBy(InfoEmpleados.class);
    }

    public List<InfoEmpleados> obtenerSupervisoresActivos() {
        return repository.findByPuesto_IdAndEstatus(idProvider.getIdPuestoSupervisor(), EmpleadoConstants.A);
    }


}