package integra.globalexception;

import integra.seguridad.usuario.exception.CreateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserAdminExceptionHandler {

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<ErrorResponse> handlerCrearUsuarioException(CreateUserException ex) {
        ErrorResponse error = new ErrorResponse("No se puede realizar esta acción", ex.getMessage(), null, null);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
