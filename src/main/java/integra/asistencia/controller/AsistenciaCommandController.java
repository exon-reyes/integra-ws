package integra.asistencia.controller;

import integra.asistencia.actions.*;
import integra.asistencia.service.jornada.*;
import integra.utils.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asistencia")
@RequiredArgsConstructor
public class AsistenciaCommandController {
    private final IniciarJornada iniciarJornada;
    private final IniciarPausa iniciarPausa;
    private final FinalizarJornada finalizarJornada;
    private final FinalizarPausa finalizarPausa;
    private final RegistroManual registroManual;

    @PostMapping("iniciar")
    public ResponseEntity<ResponseData<?>> iniciar(@Valid @RequestBody RegistroDTO dto) {
        var data = new IniciarJornadaCommand(dto.getEmpleadoId(), dto.getFoto(), dto.getUnidadId(), dto.getUnidadAsignadaId(), dto.getHora());
        iniciarJornada.execute(data);
        return ResponseEntity.ok(ResponseData.of(null, "Jornada iniciada"));
    }

    @PostMapping("/finalizar")
    public ResponseEntity<ResponseData<?>> finalizar(@Valid @RequestBody RegistroDTO dto) {
        finalizarJornada.execute(new FinalizarJornadaCommand(dto.getEmpleadoId(), dto.getFoto(), dto.getUnidadId(), dto.getFinDeposito(), dto.getUnidadAsignadaId(), null));
        return ResponseEntity.ok(ResponseData.of(null, "Jornada finalizada"));
    }

    @PostMapping("/pausa/iniciar")
    public ResponseEntity<ResponseData<?>> iniciarPausa(@Valid @RequestBody RegistroDTO dto) {
        IniciarPausaCommand command = new IniciarPausaCommand(dto.getEmpleadoId(), dto.getPausa(), dto.getFoto(), dto.getUnidadId(), dto.getUnidadAsignadaId(), null);
        iniciarPausa.execute(command);
        return ResponseEntity.ok(ResponseData.of(null, "Pausa iniciada"));
    }

    @PostMapping("/pausa/finalizar")
    public ResponseEntity<ResponseData<?>> finalizarPausa(@Valid @RequestBody RegistroDTO dto) {
        FinalizarPausaCommand command = new FinalizarPausaCommand(dto.getEmpleadoId(), dto.getPausa(), dto.getFoto(), dto.getUnidadId(), dto.getUnidadAsignadaId(), null);
        finalizarPausa.execute(command);
        return ResponseEntity.ok(ResponseData.of(null, "Pausa registrada"));
    }

    @PostMapping("/manual")
    public ResponseEntity<ResponseData<?>> registroManual(@Valid @RequestBody RegistroManualDTO request) {
        registroManual.execute(request);
        return ResponseEntity.ok(ResponseData.of(null, "Asistencia registrada"));
    }

}
