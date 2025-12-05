package integra.asistencia.service.jornada;

import integra.asistencia.actions.*;
import integra.asistencia.util.HandlerExecutor;
import integra.asistencia.util.TipoPausa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistroManual implements HandlerExecutor<Void, RegistroManualDTO> {
    private final IniciarJornada iniciarJornada;
    private final FinalizarJornada finalizarJornada;
    private final IniciarPausa iniciarPausa;
    private final FinalizarPausa finalizarPausa;

    @Override
    public Void execute(RegistroManualDTO request) {
        String tipoAccion = request.getTipoAccion().trim();
        switch (tipoAccion) {
            case "iniciarJornada" -> {
                var command = new IniciarJornadaCommand(request.getEmpleadoId(), null, request.getUnidadId(), request.getUnidadAsignadaId(), request.getHora());
                iniciarJornada.execute(command);
            }
            case "finalizarJornada" -> {
                var command = new FinalizarJornadaCommand(request.getEmpleadoId(), null, request.getUnidadId(), false, request.getUnidadAsignadaId(), request.getHora());
                finalizarJornada.execute(command);
            }
            case "finalizarJornadaDeposito" -> {
                var command = new FinalizarJornadaCommand(request.getEmpleadoId(), null, request.getUnidadId(), true, request.getUnidadAsignadaId(), request.getHora());
                finalizarJornada.execute(command);
            }
            case "iniciarPausa" -> {
                var command = new IniciarPausaCommand(request.getEmpleadoId(), TipoPausa.valueOf(request.getPausa()), null, request.getUnidadId(), request.getUnidadAsignadaId(), request.getHora());
                iniciarPausa.execute(command);
            }
            case "finalizarPausa" -> {
                var command = new FinalizarPausaCommand(request.getEmpleadoId(), TipoPausa.valueOf(request.getPausa()), null, request.getUnidadId(), request.getUnidadAsignadaId(), request.getHora());
                finalizarPausa.execute(command);
            }
            default -> throw new IllegalArgumentException("Tipo de acción no válido: " + tipoAccion);
        }
        return null;
    }
}