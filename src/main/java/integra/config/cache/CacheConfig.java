package integra.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
@Slf4j
public class CacheConfig {

    private final CacheProperties cacheProperties;
    private final MeterRegistry meterRegistry;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        log.info("======");
        log.info("Configurando cachés Caffeine...");

        cacheProperties.getSpecs().forEach((cacheName, spec) -> {

            log.info("Caché '{}' cargando configuración...", cacheName);

            Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                    .removalListener((key, value, cause) -> {
                        if (cause != RemovalCause.EXPLICIT) {
                            log.debug("Caché '{}': clave '{}' eliminada ({})", cacheName, key, cause);
                        }
                    });

            if (spec.getExpireAfterWrite() != null) {
                caffeine.expireAfterWrite(spec.getExpireAfterWrite());
            }
            if (spec.getExpireAfterAccess() != null) {
                caffeine.expireAfterAccess(spec.getExpireAfterAccess());
            }
            if (spec.getRefreshAfterWrite() != null) {
                caffeine.refreshAfterWrite(spec.getRefreshAfterWrite());
            }
            if (spec.getMaximumSize() != null) {
                caffeine.maximumSize(spec.getMaximumSize());
            }
            if (spec.getInitialCapacity() != null) {
                caffeine.initialCapacity(spec.getInitialCapacity());
            }
            if (Boolean.TRUE.equals(spec.getRecordStats())) {
                caffeine.recordStats();
            }
            if (Boolean.TRUE.equals(spec.getWeakKeys())) {
                caffeine.weakKeys();
            }
            if (Boolean.TRUE.equals(spec.getWeakValues())) {
                caffeine.weakValues();
            }
            if (Boolean.TRUE.equals(spec.getSoftValues())) {
                caffeine.softValues();
            }

            cacheManager.registerCustomCache(
                    cacheName,
                    CaffeineCacheMetrics.monitor(meterRegistry, caffeine.build(), cacheName)
            );

            log.info("Caché '{}' configurada.", cacheName);
        });

        log.info("Cachés configuradas correctamente.");
        log.info("======");
        return cacheManager;
    }
}
