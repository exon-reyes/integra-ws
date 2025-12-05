package integra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI 3.0 (Swagger) para la API de Integra
 */
@Configuration
/**
 * Configuración de OpenAPI 3.0 (Swagger) para la API de Integra
 *
 * Esta clase configura la documentación automática de la API usando Swagger/OpenAPI 3.0.
 * Define la información general de la API, incluyendo título, descripción, versión,
 * información de contacto y licencia. También configura el esquema de seguridad
 * basado en tokens JWT para la autenticación.
 */
public class OpenApiConfig {


    /**
     * Configura y personaliza la instancia de OpenAPI para la documentación de la API.
     * <p>
     * Este método define la configuración completa de la documentación OpenAPI 3.0,
     * incluyendo información general de la API, componentes de seguridad y requisitos
     * de autenticación. La configuración incluye:
     * - Información básica de la API (título, descripción, versión)
     * - Datos de contacto del equipo de desarrollo
     * - Información de licencia
     * - Esquema de seguridad basado en tokens JWT
     *
     * @return Una instancia configurada de OpenAPI con toda la información y configuración necesaria
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Integra - Sistema de Control e Información")
                        .description("API REST para el sistema de control e información de SCI")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SCI Development Team")
                                .email("dev@sci.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT en el formato: Bearer {token}")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}