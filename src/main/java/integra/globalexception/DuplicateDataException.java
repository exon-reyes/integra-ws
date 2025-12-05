package integra.globalexception;

import lombok.Getter;

/**
 * Excepción lanzada cuando se intenta crear o insertar datos que ya existen en el sistema.
 *
 * <p>Esta excepción se utiliza específicamente para casos donde la regla de negocio indica
 * que ciertos datos deben ser únicos y se ha detectado un intento de duplicarlos.</p>
 *
 * <p>Diferencia con {@link BusinessRuleException}:</p>
 * <ul>
 *   <li>{@code DuplicateDataException}: Se enfoca específicamente en violaciones de unicidad de datos</li>
 *   <li>{@link BusinessRuleException}: Se usa para cualquier otra regla de negocio general</li>
 * </ul>
 *
 * <p>Ejemplos de uso:</p>
 * <pre>
 * // Cuando un email ya está registrado en el sistema
 * if (userRepository.existsByEmail(email)) {
 *     throw new DuplicateDataException("El correo electrónico ya está registrado", "email");
 * }
 *
 * // Cuando se intenta crear un producto con código duplicado
 * if (productRepository.existsByCode(productCode)) {
 *     throw new DuplicateDataException("Ya existe un producto con ese código", "code");
 * }
 *
 * // Cuando un nombre de usuario ya existe
 * if (userRepository.existsByUsername(username)) {
 *     throw new DuplicateDataException("Nombre de usuario no disponible", "username");
 * }
 * </pre>
 */
@Getter
public class DuplicateDataException extends RuntimeException {
    private final String field;

    public DuplicateDataException(String message, String field) {
        super(message);
        this.field = field;
    }
}