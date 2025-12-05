package integra.departamento.controller;

import integra.departamento.dto.DepartamentoEntityDto;
import integra.departamento.service.DepartamentoService;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<ResponseData<List<DepartamentoEntityDto>>> obtenerDepartamentos() {
        return ResponseEntity.ok(ResponseData.of(departamentoService.obtenerDepartamentos(), "Departamentos"));
    }
}