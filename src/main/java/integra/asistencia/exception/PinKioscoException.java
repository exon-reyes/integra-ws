package integra.asistencia.exception;

/**
 * Excepción personalizada para manejar errores relacionados con el PIN.
 * Esta excepción se lanza cuando ocurren problemas con la validación o procesamiento del PIN.
 */
public class PinKioscoException extends RuntimeException {
    public PinKioscoException(String message) {
        super(message);
    }
}
