package integra.asistencia.controller;

import integra.asistencia.service.kiosco.KioscoCommandService;
import integra.asistencia.service.kiosco.KioscoQueryService;
import integra.model.Unidad;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

/**
 * Controlador REST para la gestión de kioscos.
 * <p>
 * Esta clase proporciona endpoints para administrar unidades de kiosco,
 * incluyendo operaciones como obtener información de kioscos,
 * actualizar configuraciones de cámara, generar y usar códigos de configuración,
 * y gestionar solicitudes de código de acceso.
 * <p>
 * Los endpoints cubren operaciones CRUD básicas y funcionalidades específicas
 * relacionadas con la administración y configuración remota de kioscos.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("kioscos")
public class KioscoController {
    private final KioscoQueryService queryService;
    private final KioscoCommandService commandService;

    /**
     * Obtiene todas las unidades de kiosco disponibles.
     *
     * @return Lista de unidades kiosco con mensaje de éxito
     */
    @GetMapping
    public ResponseEntity<ResponseData<List<Unidad>>> obtenerUnidadesKiosco() {
        return ResponseEntity.ok(ResponseData.of(queryService.obtenerUnidadesKiosco(), "Unidades Kiosco"));
    }


    /**
     * Actualiza el estatus de uso de la cámara para una unidad kiosco específica.
     * <p>
     * Este endpoint requiere autorización de tipo ADMIN para ejecutarse.
     * </p>
     *
     * @param id      Identificador único de la unidad kiosco
     * @param estatus Nuevo estatus de uso de la cámara (true para activo, false para inactivo)
     * @return Respuesta con confirmación de actualización exitosa
     */
    @PatchMapping("/{id}/camara")
    public ResponseEntity<ResponseData<Void>> actualizarUsoCamara(@PathVariable Integer id, @RequestParam(name = "estatus", required = true) Boolean estatus) {
        commandService.actualizarUsoCamara(id, estatus);
        return ResponseEntity.ok(ResponseData.of(true, "Estatus actualizado correctamente"));
    }


    /**
     * Genera un código de configuración de un solo uso para una unidad kiosco específica.
     * <p>
     * Este endpoint requiere autorización de tipo ADMIN para ejecutarse.
     * </p>
     *
     * @param id Identificador único de la unidad kiosco
     * @return Código de configuración generado con mensaje de confirmación
     */
    @PatchMapping("/{id}/codigo")
    public ResponseEntity<ResponseData<String>> generarCodigoConfigUnSoloUso(@PathVariable Integer id) {
        String codigo = commandService.generarCodigoConfig(id);
        return ResponseEntity.ok(ResponseData.of(codigo, "Se ha procesado la confirmación"));
    }

    /**
     * Usa un código de configuración para una unidad kiosco específica.
     *
     * @param id     Identificador único de la unidad kiosco
     * @param codigo Código de configuración a utilizar
     * @return Respuesta con confirmación de código autorizado
     */
    @PostMapping("/{id}/codigos/{codigo}/usar")
    public ResponseEntity<ResponseData<Void>> usarCodigoConfiguracion(@PathVariable Integer id, @PathVariable String codigo) {
        commandService.usarCodigoConfig(id, codigo);
        return ResponseEntity.ok(ResponseData.of(true, "Código autorizado"));
    }

    /**
     * Obtiene una unidad kiosco específica por su identificador.
     *
     * @param id Identificador único de la unidad kiosco
     * @return Unidad kiosco solicitada con mensaje de éxito
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Unidad>> obtenerUnidadKiosco(@PathVariable Integer id) {
        return ResponseEntity.ok(ResponseData.of(queryService.obtenerUnidadKiosco(id), "Unidad Kiosco"));
    }

    /**
     * Solicita un código para una unidad kiosco específica.
     *
     * @param id Identificador único de la unidad kiosco
     * @return Respuesta con confirmación de solicitud enviada
     */
    @PatchMapping("/{id}/requiere-codigo")
    public ResponseEntity<ResponseData<Void>> solicitarCodigo(@PathVariable Integer id) {
        commandService.solicitarCodigo(id);
        return ResponseEntity.ok(ResponseData.of(true, "Solicitud enviada"));
    }


    /**
     * Cancela la solicitud de código para una unidad kiosco específica.
     * <p>
     * Este endpoint requiere autorización de tipo ADMIN para ejecutarse.
     * </p>
     *
     * @param id Identificador único de la unidad kiosco
     * @return Respuesta con confirmación de cancelación de solicitud
     */
    @DeleteMapping("/{id}/requiere-codigo")
    public ResponseEntity<ResponseData<Void>> cancelarCodigo(@PathVariable Integer id) {
        commandService.cancelarRequiereCodigo(id);
        return ResponseEntity.ok(ResponseData.of(true, "Solicitud enviada"));
    }

    @PatchMapping("/{id}/compensacion")
    public ResponseEntity<ResponseData<Void>> actualizarCompensacion(@PathVariable Integer id, @RequestParam LocalTime compensacion) {
        commandService.actualizarCompensacion(id, compensacion);
        return ResponseEntity.ok(ResponseData.of(true, "Tiempo de compensación actualizado correctamente"));
    }

}
