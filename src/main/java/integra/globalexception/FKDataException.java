package integra.globalexception;

/**
 * Excepción personalizada para manejar errores relacionados con datos de clave foránea (FK).
 * <p>
 * Esta excepción se lanza cuando ocurren problemas con las restricciones de clave foránea
 * en las operaciones de base de datos, como intentar eliminar o modificar un registro
 * que está siendo referenciado por otra tabla.
 * <p>
 * Diferencias con otras excepciones personalizadas:
 * - {@link DataException}: Se enfoca en errores generales de datos no relacionados con FK
 * - {@link ValidationException}: Se utiliza para errores de validación de datos de entrada
 * - {@link BusinessException}: Se utiliza para errores de lógica de negocio general
 * <p>
 * Ejemplos de uso:
 * <pre>
 * {@code
 * // Ejemplo 1: Lanzar la excepción cuando no se puede eliminar un registro por restricción FK
 * if (hasForeignKeyReferences(entityId)) {
 *     throw new FKDataException("No se puede eliminar el registro porque está siendo referenciado por otros registros");
 * }
 *
 * // Ejemplo 2: Lanzar la excepción con información específica de la entidad
 * throw new FKDataException("No se puede eliminar el cliente con ID " + clientId + " porque tiene pedidos asociados");
 * }
 * </pre>
 */
public class FKDataException extends RuntimeException {

    /**
     * Construye una nueva excepción con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle. El mensaje se guarda para ser recuperado
     *                posteriormente por el método {@link #getMessage()}.
     */
    public FKDataException(String message) {
        super(message);
    }
}