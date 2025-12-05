package integra.globalexception.sql;

import integra.globalexception.DuplicateDataException;
import integra.globalexception.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maneja excepciones SQL comunes relacionadas con restricciones de integridad de datos.
 * Permite devolver respuestas HTTP coherentes y descriptivas ante violaciones de llaves foráneas u otras restricciones.
 */
@RestControllerAdvice
public class SQLExceptionHandler {

    /**
     * Maneja violaciones de integridad referencial (llaves foráneas).
     */
    @ExceptionHandler(ForeignKeyConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleForeignKeyViolation(ForeignKeyConstraintViolationException ex) {
        ErrorResponse error = new ErrorResponse("No se puede realizar esta acción", "Este registro está en uso en otras partes del sistema. Por favor, elimina o actualiza los datos relacionados primero.", HttpStatus.CONFLICT, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Maneja violaciones generales de integridad de datos.
     * Detecta si la causa es una llave foránea y devuelve un error 409 Conflict.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String causeMessage = ex.getMostSpecificCause().getMessage().toLowerCase();

        if (causeMessage.contains("foreign key")) {
            ErrorResponse error = new ErrorResponse("No se puede realizar esta acción", "Este registro está en uso en otras partes del sistema. Por favor, elimina o actualiza los datos relacionados primero.", HttpStatus.CONFLICT, null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } else if (causeMessage.contains("duplicate entry")) {
            ErrorResponse error = new ErrorResponse("Valor duplicado", "Ya existe un registro con este valor. Por favor, ingresa un valor diferente.", HttpStatus.CONFLICT, null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        ErrorResponse error = new ErrorResponse("No se puede guardar el registro", "Hubo un problema al guardar los datos. Por favor, verifica la información e intenta nuevamente.", HttpStatus.CONFLICT, null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDataException(DuplicateDataException ex) {
        ErrorResponse error = new ErrorResponse("Valor duplicado", ex.getMessage(), HttpStatus.CONFLICT, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
