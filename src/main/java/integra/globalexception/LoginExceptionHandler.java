package integra.globalexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoginExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(LoginException ex) {
        ErrorResponse response = new ErrorResponse(ex.getTitle(), ex.getMessage(), HttpStatus.UNAUTHORIZED, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
