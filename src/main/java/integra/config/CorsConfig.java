package integra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Clase de Configuración de CORS (Cross-Origin Resource Sharing).
 * <p>
 * Esta clase de configuración establece las políticas de CORS para la aplicación,
 * permitiendo un acceso controlado a los recursos desde diferentes orígenes.
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 * {@code
 * // Esta configuración será recogida automáticamente por Spring
 * // y aplicada a todos los endpoints de la aplicación
 * }
 * </pre>
 *
 * @author Equipo Exon
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class CorsConfig {

    /**
     * Crea y configura un bean fuente de configuración CORS.
     * <p>
     * Este método define qué orígenes, métodos y cabeceras están permitidos
     * al realizar solicitudes cross-origin a la aplicación.
     *
     * <p>La configuración incluye:</p>
     * <ul>
     *   <li>Permite todos los orígenes (*) - debe restringirse en producción</li>
     *   <li>Permite métodos HTTP comunes: GET, POST, PUT, DELETE, OPTIONS</li>
     *   <li>Permite todas las cabeceras (*)</li>
     *   <li>Deshabilita credenciales (cookies, cabeceras de autorización)</li>
     * </ul>
     *
     * <p>Ejemplo de solicitud CORS que sería permitida:</p>
     * <pre>
     * {@code
     * GET /api/users HTTP/1.1
     * Host: api.example.com
     * Origin: https://www.example.com
     * Access-Control-Request-Method: GET
     * }
     * </pre>
     *
     * @return una fuente de configuración CORS configurada que permite solicitudes cross-origin
     * @see CorsConfigurationSource
     * @see CorsConfiguration
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Para producción, especificar orígenes exactos
        configuration.setAllowedMethods(Arrays.asList("GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",
                configuration);
        return source;
    }
}