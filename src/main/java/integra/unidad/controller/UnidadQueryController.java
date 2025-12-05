package integra.unidad.controller;

import integra.model.HorarioOperativo;
import integra.model.Unidad;
import integra.security.Autoridades;
import integra.unidad.service.UnidadQueryService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("unidades")
@RequiredArgsConstructor
class UnidadQueryController {
    private final UnidadQueryService queryService;

    @GetMapping
    @PreAuthorize(Autoridades.CONSULTAR_UNIDADES)
    public ResponseEntity<ResponseData<List<Unidad>>> obtenerUnidades() {
        return ResponseEntity.ok(ResponseData.of(queryService.obtenerInfoBasica(), "Información básica"));
    }

    @GetMapping("activas")
    @PreAuthorize(Autoridades.CONSULTAR_UNIDADES)
    public ResponseEntity<ResponseData<List<Unidad>>> obtenerUnidadesActivas() {
        return ResponseEntity.ok(ResponseData.of(queryService.obtenerUnidadesActivas(), "Unidades activas"));
    }

    @GetMapping("supervisor/{id}")
    public ResponseEntity<ResponseData<List<Unidad>>> obtenerUnidadesPorSupervisor(@PathVariable Integer id) {
        return ResponseEntity.ok(ResponseData.of(queryService.obtenerInfoBasicaPorSupervisor(id), "Información básica"));
    }

    @GetMapping("contacto/{id}")
    @PreAuthorize(Autoridades.CONSULTAR_UNIDADES)
    public ResponseEntity<ResponseData<Unidad>> obtenerContactoUnidad(@PathVariable Integer id) {
        return ResponseEntity.ok(ResponseData.of(queryService.obtenerContacto(id), "Información de contacto"));
    }

    @GetMapping("horario/{id}")
    public ResponseEntity<ResponseData<List<HorarioOperativo>>> obtenerHorarioUnidad(@PathVariable Integer id) {
        return ResponseEntity.ok(ResponseData.of(queryService.obtenerHorario(id), "Información de horario"));
    }

    @GetMapping("exportar")
    @PreAuthorize(Autoridades.EXPORTAR_UNIDAD)
    public ResponseEntity<byte[]> exportarUnidades() {
        byte[] excelData = queryService.exportarUnidadesExcel();
        return ResponseEntity.ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition", "attachment; filename=unidades.xlsx")
                .body(excelData);
    }
}