package integra.seguridad.usuario.service;

import integra.seguridad.usuario.entity.TokenVersion;
import integra.seguridad.usuario.repository.TokenVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de la gestión de versiones de token por usuario.
 * <p>
 * Cada usuario posee una versión de token que permite invalidar sus JWTs
 * emitidos previamente cuando cambian sus permisos o roles. Esta información
 * se almacena en base de datos y se mantiene en caché (Caffeine) para mejorar
 * el rendimiento de lectura.
 * </p>
 *
 * <h2>Flujo general:</h2>
 * <ol>
 *     <li>Durante el login, se consulta la versión actual del usuario mediante {@link #getVersion(String)}.</li>
 *     <li>El valor devuelto se incluye como claim <b>"ver"</b> dentro del JWT.</li>
 *     <li>En la validación del token, se compara el valor de <b>"ver"</b> con la versión actual en caché.</li>
 *     <li>Si las versiones no coinciden, el token se considera inválido (p. ej., por cambio de permisos).</li>
 * </ol>
 *
 * <p>El cacheo de las versiones utiliza la anotación {@link Cacheable}, y las
 * operaciones de actualización o limpieza utilizan {@link CacheEvict} para
 * asegurar coherencia entre base de datos y memoria.</p>
 *
 * @author Exon
 * @version 1.0
 * @since 2025-11-04
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenVersionService {

    private final TokenVersionRepository repository;

    /**
     * Obtiene la versión actual del token para el usuario especificado.
     * <p>
     * Si el usuario no tiene un registro previo en la tabla <b>token_version</b>,
     * se crea automáticamente con una versión inicial de {@code 1}.
     * </p>
     *
     * @param username nombre de usuario.
     * @return número de versión actual del token del usuario.
     */
    @Cacheable(value = "tokenVersions", key = "#username")
    public int getVersion(String username) {
        return repository.findByUsername(username)
                .map(TokenVersion::getVersion)
                .orElseGet(() -> repository.save(new TokenVersion(null, username, 1)).getVersion());
    }

    /**
     * Incrementa la versión del token asociada a un usuario específico.
     * <p>
     * Esta operación invalida automáticamente los tokens previos, ya que al
     * cambiar la versión, los JWT existentes dejarán de coincidir con la versión
     * almacenada.
     * </p>
     * <p>
     * El valor cacheado se elimina usando {@link CacheEvict}, de modo que la
     * próxima lectura consulte el valor actualizado en base de datos.
     * </p>
     *
     * @param username nombre del usuario.
     * @return nueva versión del token después del incremento.
     */
    @Transactional
    @CacheEvict(value = "tokenVersions", key = "#username")
    public int incrementVersion(String username) {
        var tokenVersion = repository.findByUsername(username).map(tv -> {
            tv.setVersion(tv.getVersion() + 1);
            return repository.save(tv);
        }).orElseGet(() -> repository.save(new TokenVersion(null, username, 1)));
        return tokenVersion.getVersion();
    }

    /**
     * Elimina todas las entradas del caché de versiones de token.
     * <p>
     * Puede ser invocado, por ejemplo, al realizar una actualización masiva
     * de roles o al reiniciar el sistema.
     * </p>
     */
    @CacheEvict(value = "tokenVersions", allEntries = true)
    public void evictAllCache() {
        // Intencionalmente vacío; la anotación maneja la invalidación del caché.
    }

    /**
     * Precarga en el caché todas las versiones de token existentes en base de datos.
     * <p>
     * Este método puede llamarse durante el arranque del sistema (por ejemplo,
     * al recibir el evento {@link ApplicationReadyEvent}) para inicializar
     * las versiones en memoria y evitar la latencia de la primera consulta.
     * </p>
     */
    public void preloadCache() {
        repository.findAll().forEach(tv -> getVersion(tv.getUsername()));
    }
}
