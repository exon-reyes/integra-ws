package integra.asistencia.factory;


import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.actions.EmpleadoReporteRequest;

public final class EmpleadoFactory {
    /**
     * Mapea el objeto de la solicitud HTTP a un objeto de comando interno.
     */
    public static EmpleadoReporteCommand mapRequestToCommand(EmpleadoReporteRequest request) {
        EmpleadoReporteCommand command = new EmpleadoReporteCommand();
        command.setEmpleadoId(request.getEmpleadoId());
        command.setDesde(request.getDesde());
        command.setHasta(request.getHasta());
        command.setUnidadId(request.getUnidadId());
        command.setZonaId(request.getZonaId());
        command.setSupervisorId(request.getSupervisorId());
        command.setPuestoId(request.getPuestoId());
        return command;
    }
}
