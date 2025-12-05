package integra.globalexception;

import integra.asistencia.exception.AsistenciaException;
import integra.asistencia.exception.PinKioscoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KioscoHandlerException {
    @ExceptionHandler(AsistenciaException.class)
    public ResponseEntity<ErrorResponse> asistenciaException(AsistenciaException ex) {
        ErrorResponse error = new ErrorResponse("No se puede realizar esta acción", ex.getMessage(), HttpStatus.CONFLICT, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(PinKioscoException.class)
    public ResponseEntity<ErrorResponse> pinKioscoException(PinKioscoException ex) {
        ErrorResponse error = new ErrorResponse("No se puede realizar esta acción", ex.getMessage(), HttpStatus.CONFLICT, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
