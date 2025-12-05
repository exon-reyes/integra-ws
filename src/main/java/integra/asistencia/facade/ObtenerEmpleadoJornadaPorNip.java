package integra.asistencia.facade;

import integra.asistencia.actions.EmpleadoJornada;
import integra.asistencia.entity.PausaModel;
import integra.asistencia.exception.PinKioscoException;
import integra.asistencia.query.EmpleadoModelInfo;
import integra.asistencia.repository.AsistenciaRepository;
import integra.asistencia.repository.PausaModelRepository;
import integra.asistencia.service.EmpleadoPuestoValidatorService;
import integra.asistencia.util.HandlerExecutor;
import integra.empleado.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ObtenerEmpleadoJornadaPorNip implements HandlerExecutor<EmpleadoJornada, String> {
    private final EmpleadoRepository empleadoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final PausaModelRepository pausaRepository;
    private final EmpleadoPuestoValidatorService empleadoPuestoRepositoryImpl;

    @Override
    public EmpleadoJornada execute(String pin) {
        // 1. Obtener la información básica del empleado
        EmpleadoModelInfo data = empleadoRepository.findByPin(pin, EmpleadoModelInfo.class)
                .orElseThrow(() -> new PinKioscoException("No se encontró al empleado especificado por el NIP " + pin));

        // 2. Determinar si la jornada está iniciada
        boolean esNocturno = empleadoPuestoRepositoryImpl.tienePuestoNocturno(data.puestoId());
        boolean jornadaIniciada = esNocturno ? empleadoPuestoRepositoryImpl.tieneJornadaActivaNocturna(data.id()) : asistenciaRepository.findFirstByEmpleado_IdAndJornadaCerradaFalseOrderByInicioJornadaDesc(data.id())
                .isPresent();

        // 3. Buscar la pausa activa
        String tipoPausa = pausaRepository.findFirstByAsistencia_Empleado_IdAndFinNullOrderByInicioDesc(data.id())
                .map(PausaModel::getTipo) // Extrae el tipo de la pausa (ej: "COMIDA")
                .orElse(null);          // Si no hay pausa, el valor es null
        // 4. Construir el nombre completo
        String nombreCompleto = data.nombre() + " " + data.apellidoPaterno() + " " + data.apellidoMaterno();
        // 5. Devolver el objeto EmpleadoJornada con la nueva información
        return new EmpleadoJornada(data.id(), data.codigoEmpleado(), nombreCompleto, jornadaIniciada, esNocturno, tipoPausa, data.unidadId());
    }
}
