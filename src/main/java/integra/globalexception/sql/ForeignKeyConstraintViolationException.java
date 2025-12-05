package integra.globalexception.sql;

public class ForeignKeyConstraintViolationException extends RuntimeException {
    public ForeignKeyConstraintViolationException(String message) {
        super(message);
    }

    public ForeignKeyConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
