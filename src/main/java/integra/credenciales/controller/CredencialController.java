package integra.credenciales.controller;

import integra.credenciales.query.CuentaEntityDto;
import integra.credenciales.request.FiltroCuenta;
import integra.credenciales.request.NuevaCuenta;
import integra.credenciales.request.TipoCuenta;
import integra.credenciales.service.CuentaCommandService;
import integra.credenciales.service.CuentaQueryService;
import integra.utils.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("credenciales")
@RestController
@RequiredArgsConstructor
public class CredencialController {
    private final CuentaQueryService cuentaQueryService;
    private final CuentaCommandService cuentaCommandService;

    @GetMapping
    public ResponseEntity<ResponseData<List<CuentaEntityDto>>> obtenerCuentasPorFiltro(FiltroCuenta filtro) {
        return ResponseEntity.ok(ResponseData.of(cuentaQueryService.obtenerCuentasPorFiltro(filtro), "Cuentas registradas"));
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> registrarCuenta(@Valid @RequestBody NuevaCuenta data) {
        cuentaCommandService.registrar(data);
        return ResponseEntity.ok(ResponseData.of(null, "Cuenta registrada"));
    }

    @PostMapping("tipo")
    public ResponseEntity<ResponseData<Void>> registrarTipoCuenta(@Valid @RequestBody TipoCuenta data) {
        cuentaCommandService.registrarTipoCuenta(data);
        return ResponseEntity.ok(ResponseData.of(null, "Tipo registrado"));
    }

    @PutMapping("tipo/{id}")
    public ResponseEntity<ResponseData<Void>> actualizarTipoCuenta(@PathVariable Integer id, @Valid @RequestBody TipoCuenta data) {
        cuentaCommandService.actualizarTipoCuenta(id, data);
        return ResponseEntity.ok(ResponseData.of(null, "Tipo actualizado"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseData<Void>> eliminarCuenta(@PathVariable Integer id) {
        cuentaCommandService.eliminar(id);
        return ResponseEntity.ok(ResponseData.of(null, "Cuenta eliminada"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseData<Void>> actualizarCuenta(@PathVariable Integer id, @Valid @RequestBody NuevaCuenta data) {
        cuentaCommandService.actualizar(id, data);
        return ResponseEntity.ok(ResponseData.of(null, "Cuenta actualizada"));
    }

    @GetMapping("tipos")
    public ResponseEntity<ResponseData<List<?>>> obtenerTipos() {
        return ResponseEntity.ok(ResponseData.of(cuentaQueryService.obtenerTiposCuenta(), "Tipos de cuentas"));
    }

    @DeleteMapping("tipo/{id}")
    public ResponseEntity<ResponseData<Void>> eliminarTipoCuenta(@PathVariable Integer id) {
        cuentaCommandService.eliminarTipoCuenta(id);
        return ResponseEntity.ok(ResponseData.of(null, "Tipo eliminado"));
    }
}
