package integra.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("Error al obtener datos del caché '{}' con la clave '{}': {}", cache.getName(), key, exception.getMessage(), exception);
        // Degradación controlada: no relanzar la excepción, la aplicación continúa sin caché
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("Error al guardar en el caché '{}' con la clave '{}': {}", cache.getName(), key, exception.getMessage(), exception);
        // Degradación controlada: no relanzar la excepción
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("Error al eliminar del caché '{}' con la clave '{}': {}", cache.getName(), key, exception.getMessage(), exception);
        // Degradación controlada: no relanzar la excepción
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("Error al limpiar el caché '{}': {}", cache.getName(), exception.getMessage(), exception);
        // Degradación controlada: no relanzar la excepción
    }
}