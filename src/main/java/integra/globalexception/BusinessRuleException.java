package integra.globalexception;

import lombok.Getter;

/**
 * BusinessRuleException se utiliza para indicar violaciones de reglas de negocio en la aplicación.
 * Esta excepción debe ser lanzada cuando una operación de negocio no puede completarse debido
 * a restricciones o reglas de negocio.
 *
 * <p>Utilice esta excepción cuando necesite:</p>
 * <ul>
 *   <li>Indicar que una operación no está disponible bajo las condiciones actuales</li>
 *   <li>Señalar que las reglas de negocio impiden continuar con una solicitud</li>
 *   <li>Devolver un mensaje de error amigable para el usuario con un título específico</li>
 * </ul>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 * // Con título personalizado y mensaje
 * throw new BusinessRuleException("Saldo Insuficiente", "El saldo de la cuenta no es suficiente para esta transacción");
 *
 * // Con título predeterminado ("Operación no disponible")
 * throw new BusinessRuleException("No se puede procesar el reembolso para una orden cancelada");
 * </pre>
 */
@Getter
public class BusinessRuleException extends RuntimeException {
    private final String title;

    public BusinessRuleException(String title, String message) {
        super(message);
        this.title = title;
    }

    public BusinessRuleException(String message) {
        super(message);
        this.title = "Operación no disponible";
    }

}