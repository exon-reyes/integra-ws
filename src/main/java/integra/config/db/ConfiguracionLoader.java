package integra.config.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConfiguracionLoader {


    private final ConfiguracionService configuracionService;
    private final ConfigurableEnvironment env; // Inyección del Entorno de Spring

    @EventListener
    public void handleContextRefreshed(ContextRefreshedEvent event) {
        // 1. Carga los parámetros desde la DB
        Map<String, Object> parametrosDb = configuracionService.cargarParametrosGlobales();

        // 📢 NUEVO LOG DE DIAGNÓSTICO
        System.out.println("=======");
        System.out.println("🚨 DIAGNÓSTICO: CLAVES DEL MAPA DE LA DB ANTES DE INYECCIÓN:");

        // Imprime las claves que se están inyectando. ¡Busca 'app.config.key'!
        parametrosDb.forEach((k, v) -> System.out.println(" -> CLAVE: " + k + " | VALOR: " + v));
        System.out.println("=======");

        // 2. Crea una nueva fuente de propiedades para la DB
        MapPropertySource dbPropertySource = new MapPropertySource("dbProperties", parametrosDb);

        // 3. Añade la nueva fuente de propiedades
        env.getPropertySources().addFirst(dbPropertySource);
        System.out.println("✅ Configuración de la DB cargada correctamente en Spring Environment.");
        System.out.println("=======");
    }
}