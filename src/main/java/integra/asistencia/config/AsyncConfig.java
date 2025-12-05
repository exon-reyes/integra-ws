package integra.asistencia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Clase de configuración para la gestión de tareas asíncronas en la aplicación.
 * <p>
 * Utiliza la anotación {@code @EnableAsync} para habilitar el procesamiento asíncrono
 * de métodos anotados con {@code @Async} dentro de los componentes de Spring.
 * </p>
 *
 * @author Pablo Reyes
 * @since 1.0.0
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Define y configura el ejecutor de tareas ({@code Executor}) para el registro
     * de incidencias provenientes del quiosco de asistencia.
     * <p>
     * Este ThreadPoolTaskExecutor se utiliza específicamente para el método
     * {@code registrarIncidenciaKioscoAsync} en el servicio de verificación,
     * permitiendo que la lógica de persistencia se ejecute en un hilo separado.
     * </p>
     *
     * @return Una instancia de {@link Executor} configurada para el manejo asíncrono.
     * @see integra.asistencia.service.UnidadVerificadorService#registrarIncidenciaKioscoAsync(Integer, Integer, Integer, Integer, String, integra.asistencia.entity.TipoIncidencia, String)
     */
    @Bean(name = "UnidadRegistroKiosco")
    public Executor unidadRegistroKioscoExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Número de hilos que permanecen en el pool sin importar que estén inactivos
        executor.setCorePoolSize(2);

        // Número máximo de hilos que el pool puede crear
        executor.setMaxPoolSize(8);

        // Capacidad de la cola que almacena tareas cuando el corePoolSize está lleno
        executor.setQueueCapacity(100);

        // Prefijo de los nombres de los hilos para facilitar la monitorización
        executor.setThreadNamePrefix("UnidadRegistroKiosco-");

        executor.initialize();
        return executor;
    }
}