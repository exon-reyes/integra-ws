package integra.globalexception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ErrorResponse(String title, String message, HttpStatus status, Map<String, Object> details) {
}
