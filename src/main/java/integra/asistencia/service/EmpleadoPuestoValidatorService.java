package integra.asistencia.service;

import integra.asistencia.repository.AsistenciaRepository;
import integra.asistencia.repository.EmpleadoPuestoService;
import integra.config.db.SystemIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class EmpleadoPuestoValidatorService implements EmpleadoPuestoService {
    private final AsistenciaRepository asistenciaRepository;
    private final SystemIdProvider globalConfig;

    @Override
    public boolean tienePuestoNocturno(Integer idPuestoEmpleado) {
        if (idPuestoEmpleado == null) return false;
        return idPuestoEmpleado.equals(globalConfig.getIdPuestoNocturno());
    }

    @Override
    public boolean tieneJornadaActivaNocturna(Integer empleadoId) {
        LocalTime horaCorte = globalConfig.getHoraInicioNocturno();
        LocalDateTime desde = LocalDate.now().minusDays(1).atTime(horaCorte);
        LocalDateTime hasta = LocalDateTime.now();

        return asistenciaRepository.findByEmpleado_IdAndJornadaCerradaFalse(empleadoId)
                .stream()
                .anyMatch(a -> a.getInicioJornada() != null && !a.getJornadaCerrada() && a.getInicioJornada()
                        .isAfter(desde) && a.getInicioJornada().isBefore(hasta));
    }
}
