package integra.asistencia.repository;

public interface EmpleadoPuestoService {
    boolean tienePuestoNocturno(Integer idPuestoEmpleado);

    boolean tieneJornadaActivaNocturna(Integer empleadoId);
}
