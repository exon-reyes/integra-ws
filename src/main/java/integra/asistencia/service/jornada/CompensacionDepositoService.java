package integra.asistencia.service.jornada;

import integra.asistencia.actions.FinalizarJornadaCommand;
import integra.asistencia.entity.AsistenciaModel;
import integra.asistencia.entity.CompensacionSalidaDepositoEntity;
import integra.asistencia.model.PausaAsistencia;
import integra.asistencia.util.CalculadoraJornada;
import integra.asistencia.util.ConvertidorPausas;
import integra.empleado.EmpleadoEntity;
import integra.unidad.entity.UnidadEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class CompensacionDepositoService {

    public CompensacionSalidaDepositoEntity crearCompensacion(AsistenciaModel asistencia, FinalizarJornadaCommand command, int minutosFaltantes, int minutosCompensacion) {
        CompensacionSalidaDepositoEntity compensacion = new CompensacionSalidaDepositoEntity();

        LocalDateTime ahora = LocalDateTime.now();
        List<PausaAsistencia> pausas = ConvertidorPausas.mapearPausas(asistencia.getPausas());

        compensacion.setAsistencia(new AsistenciaModel(asistencia.getId()));
        compensacion.setEmpleado(new EmpleadoEntity(command.empleadoId()));
        compensacion.setUnidad(new UnidadEntity(command.unidadId()));
        compensacion.setFecha(LocalDate.now());
        compensacion.setHoraSalida(ahora.toLocalTime());

        // Usar CalculadoraJornada para cálculos precisos
        compensacion.setHorasTrabajadas(calcularHorasTrabajadas(asistencia, ahora, pausas));
        compensacion.setHorasFaltantes(convertirMinutosALocalTime(minutosFaltantes));
        compensacion.setTiempoCompensado(convertirMinutosALocalTime(minutosCompensacion));

        return compensacion;
    }

    /**
     * Calcula las horas realmente trabajadas usando CalculadoraJornada
     * que descuenta automáticamente las pausas
     */
    private LocalTime calcularHorasTrabajadas(AsistenciaModel asistencia, LocalDateTime finJornada, List<PausaAsistencia> pausas) {
        var tiempoNeto = CalculadoraJornada.calcularTiempoNeto(asistencia.getInicioJornada(), finJornada, pausas);
        return convertirMinutosALocalTime(tiempoNeto.minutos());
    }

    private LocalTime convertirMinutosALocalTime(int minutos) {
        int horas = minutos / 60;
        int minutosRestantes = minutos % 60;
        return LocalTime.of(horas, minutosRestantes);
    }
}