package integra.globalexception;

import lombok.Getter;

/**
 * Excepción lanzada cuando los datos solicitados no se encuentran en el sistema.
 * <p>
 * Esta excepción debe utilizarse cuando:
 * - Un registro o entidad específica no puede ser localizada por su identificador
 * - Las operaciones de búsqueda devuelven ningún resultado cuando se espera al menos un resultado
 * - Las dependencias de datos requeridas están ausentes durante el procesamiento
 * - Los datos que deberían existir según la lógica del negocio están ausentes
 * <p>
 * Ejemplo de uso:
 * throw new DataNotFoundException("Usuario No Encontrado", "No existe usuario con ID: " + userId);
 */
@Getter
public class DataNotFoundException extends RuntimeException {
    private final String title;

    public DataNotFoundException(String title, String description) {
        super(description);
        this.title = title;
    }
}
