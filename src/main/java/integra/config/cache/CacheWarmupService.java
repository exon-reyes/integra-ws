package integra.config.cache;

import integra.seguridad.rol.service.RolesPermisosService;
import integra.seguridad.usuario.service.TokenVersionService;
import integra.unidad.service.UnidadQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de realizar el "precalentamiento" (warmup) de las cachés del sistema
 * una vez que la aplicación ha iniciado completamente.
 *
 * <p>El objetivo principal de este servicio es cargar en memoria ciertos datos críticos o de uso
 * frecuente, de manera que las primeras peticiones al backend no experimenten latencia
 * causada por una caché vacía.</p>
 *
 * <p>Este proceso se ejecuta automáticamente al recibir el evento
 * {@link ApplicationReadyEvent}, lo que garantiza que todos los componentes de Spring
 * ya estén inicializados antes de comenzar el precalentamiento.</p>
 *
 * <p>En caso de ocurrir un error durante la carga de datos, el proceso será registrado
 * en el log pero no impedirá que la aplicación continúe con su arranque normal.</p>
 *
 * @author Pablo Reyes
 * @version 1.0.1-Beta
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CacheWarmupService {

    /**
     * Servicio encargado de las consultas de información de unidades.
     */
    private final UnidadQueryService unidadQueryService;
    private final TokenVersionService tokenVersionService;

    /**
     * Servicio encargado de las consultas de roles y permisos.
     */
    private final RolesPermisosService rolesPermisosService;

    /**
     * Método que se ejecuta automáticamente al completar el arranque de la aplicación.
     * <p>
     * Realiza las llamadas necesarias a los servicios que cargan las cachés principales
     * del sistema, permitiendo un acceso más rápido a los datos desde el inicio.
     * </p>
     */
    @EventListener(ApplicationReadyEvent.class)
    public void warmupCaches() {
        log.info("======");
        log.info("Iniciando proceso de precalentamiento de cachés...");

        try {
            // === Carga inicial de cachés de Unidad ===
            log.info("Precargando caché de información básica de unidades...");
            unidadQueryService.obtenerInfoBasica();

            log.info("Precargando caché de unidades activas...");
            unidadQueryService.obtenerUnidadesActivas();

            // === Carga inicial de cachés de Roles y Permisos ===
            log.info("Precargando caché de roles y permisos...");
            rolesPermisosService.obtenerRolesConPermisos();

            log.info("Precargando caché de versión de tokens");
            tokenVersionService.preloadCache();
            // Puedes agregar aquí más invocaciones para otros servicios con caché
            // Ejemplo: productoService.precargarProductos();
            // Ejemplo: clienteService.cargarClientesFrecuentes();
            log.info("El precalentamiento de cachés se completó exitosamente");
            log.info("======");
        } catch (Exception e) {
            log.error("Error durante el proceso de precalentamiento de cachés. "
                    + "La aplicación continuará su ejecución normalmente.", e);
            // No se lanza la excepción para no interrumpir el arranque del sistema
        }
    }
}
