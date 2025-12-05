package integra.empleado;

import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("empleados")
class EmpleadoController {
    private final EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<ResponseData<List<InfoBasicaEmpleado>>> obtenerEmpleado() {
        return ResponseEntity.ok(ResponseData.of(empleadoService.obtenerInfoBasicaEmpleados(), "Empleado"));
    }

    @GetMapping("filtrados")
    public ResponseEntity<ResponseData<List<InfoBasicaEmpleado>>> obtenerEmpleadosFiltrados(EmpleadoFiltros filtros) {
        return ResponseEntity.ok(ResponseData.of(empleadoService.obtenerEmpleadosFiltrados(filtros), "Empleado"));
    }

    @GetMapping("nombres")
    public ResponseEntity<ResponseData<List<InfoEmpleados>>> obtenerSoloEmpleados() {
        return ResponseEntity.ok(ResponseData.of(empleadoService.obtenerSoloEmpleados(), "Empleado"));
    }

    @GetMapping("supervisores-activos")
    public ResponseEntity<ResponseData<List<InfoEmpleados>>> obtenerSupervisoresActivos() {
        return ResponseEntity.ok(ResponseData.of(empleadoService.obtenerSupervisoresActivos(), "Supervisores activos"));
    }

}
