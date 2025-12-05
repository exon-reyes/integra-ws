package integra.config.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
@Slf4j
public class PropertiesInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("======");
        log.info("⬇️ Inicializando archivo business.properties");
        File configDir = new File("./integra-config");
        File propertiesFile = new File(configDir, "business.properties");

        if (!propertiesFile.exists()) {
            log.info("⚠️ Archivo business.properties no encontrado. Creando con configuración predeterminada");
            if (configDir.mkdirs()) {
                createDefaultPropertiesFile(propertiesFile);
            } else {
                log.error("⚠️ Error al crear el directorio de configuración");
                throw new RuntimeException("Error al crear el directorio de configuración");
            }
        }
        log.info("======");
    }

    private void createDefaultPropertiesFile(File file) throws IOException {

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("# Configuración de corte de jornadas predeterminadas\n\n");
            writer.write("jornada.corte.nocturno=0\n");
            writer.write("jornada.corte.normal=0\n");
            log.info("Archivo business.properties creado con config. predeterminada creado con éxito");
        }
    }
}