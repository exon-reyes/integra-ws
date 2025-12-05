package integra.asistencia.controller;

import integra.asistencia.actions.EmpleadoJornada;
import integra.asistencia.exception.PinKioscoException;
import integra.asistencia.facade.ObtenerEmpleadoJornadaPorNip;
import integra.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsable de exponer los endpoints de consulta
 * relacionados con la asistencia y jornadas laborales de los empleados.
 *
 * <p>Esta clase actúa como un adaptador web dentro de la arquitectura hexagonal,
 * delegando la lógica de negocio al caso de uso {@link ObtenerEmpleadoJornadaPorNip}.
 * </p>
 *
 * <p>Permite obtener la información de la jornada de un empleado a partir de su NIP,
 * validando previamente la integridad del parámetro y encapsulando las respuestas
 * dentro de un objeto genérico {@link ResponseData}.</p>
 *
 * @author Pablo Reyes
 * @version 1.1
 * @since 1.0
 */
@RestController
@RequestMapping("/asistencia")
@RequiredArgsConstructor
public class AsistenciaQueryController {

    private final ObtenerEmpleadoJornadaPorNip empleadoJornadaPorNip;

    /**
     * Obtiene la información de la jornada laboral de un empleado a partir de su NIP.
     *
     * <p>Este endpoint permite consultar los datos asociados a la jornada del empleado,
     * validando que el parámetro recibido no sea nulo ni vacío antes de procesar la solicitud.</p>
     *
     * <p>En caso de recibir un NIP inválido, se lanza una {@link PinKioscoException}.
     * Cualquier otro error inesperado durante la ejecución genera una {@link RuntimeException} genérica.</p>
     *
     * <h4>Ejemplo de solicitud:</h4>
     * <pre>{@code
     * GET /asistencia/1234
     * }</pre>
     *
     * <h4>Ejemplo de respuesta exitosa:</h4>
     * <pre>{@code
     * {
     *   "success": true,
     *   "message": "Jornada de empleado consultado",
     *   "data": {
     *       "idEmpleado": 1,
     *       "nombre": "Juan Pérez",
     *       "horaEntrada": "08:00",
     *       "horaSalida": "17:00"
     *       "unidadId":63
     *   }
     * }
     * }</pre>
     *
     * @param nip el número de identificación personal (NIP) del empleado;
     *            no puede ser nulo ni vacío.
     * @return una respuesta HTTP 200 (OK) con la jornada del empleado encapsulada
     * en un {@link ResponseData}.
     * @throws PinKioscoException si el NIP es nulo o está vacío.
     * @throws RuntimeException   si ocurre un error durante la obtención de la jornada.
     */
    @GetMapping("/{nip}")
    public ResponseEntity<ResponseData<EmpleadoJornada>> consultar(@PathVariable String nip) {
        if (nip == null || nip.trim().isEmpty()) {
            throw new PinKioscoException("El pin no puede estar vacío");
        }

        try {
            EmpleadoJornada result = empleadoJornadaPorNip.execute(nip);
            return ResponseEntity.ok(ResponseData.of(result, "Jornada de empleado consultado"));
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar la jornada del empleado", e);
        }
    }

}
