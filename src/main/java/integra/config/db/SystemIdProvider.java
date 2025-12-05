package integra.config.db;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * Servicio proveedor de configuraciones y constantes del sistema.
 * <p>
 * Esta clase actúa como una fachada sobre {@link Environment} para centralizar
 * el acceso a propiedades críticas de la aplicación. Proporciona métodos tipados
 * y valores por defecto (fallback) en caso de que las propiedades no estén
 * definidas en los archivos de configuración (application.yml).
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SystemIdProvider {

    private final Environment env;

    /**
     * Obtiene el ID del puesto de trabajo correspondiente al turno nocturno.
     * <p>
     * Propiedad: {@code app.config.idPuestoNocturno}
     * </p>
     *
     * @return El ID del puesto nocturno. Valor por defecto: <b>2</b>.
     */
    public Integer getIdPuestoNocturno() {
        return env.getProperty("app.config.idPuestoNocturno", Integer.class, 2);
    }

    /**
     * Obtiene la hora de inicio configurada para el turno nocturno.
     * <p>
     * Propiedad: {@code app.config.horaInicioNocturno}
     * <br>
     * Formato esperado: "HH:mm" (ej. "18:00").
     * </p>
     *
     * @return La hora de inicio como {@link LocalTime}. Valor por defecto: <b>18:00</b>.
     */
    public LocalTime getHoraInicioNocturno() {
        String horaStr = env.getProperty("app.config.horaInicioNocturno", "18:00");
        return LocalTime.parse(horaStr);
    }

    /**
     * Obtiene el nombre del rol que se asigna automáticamente a los usuarios nuevos.
     * <p>
     * Propiedad: {@code app.config.defaultRolUsuarioNuevo}
     * </p>
     *
     * @return El nombre del rol. Valor por defecto: <b>"Vendedor"</b>.
     */
    public String getDefaultRolUsuarioNuevo() {
        return env.getProperty("app.config.defaultRolUsuarioNuevo", "Vendedor");
    }

    /**
     * Obtiene el ID del puesto de trabajo correspondiente al Supervisor.
     * <p>
     * Propiedad: {@code app.config.idPuestoSupervisor}
     * </p>
     *
     * @return El ID del puesto de supervisor. Valor por defecto: <b>4</b>.
     */
    public Integer getIdPuestoSupervisor() {
        return env.getProperty("app.config.idPuestoSupervisor", Integer.class, 4);
    }

    /**
     * Obtiene el ID del usuario administrador principal del sistema.
     * <p>
     * Propiedad: {@code app.config.idUsuarioAdmin}
     * </p>
     *
     * @return El ID del usuario admin. Valor por defecto: <b>1L</b>.
     */
    public Long getIdUsuarioAdmin() {
        return env.getProperty("app.config.idUsuarioAdmin", Long.class, 1L);
    }
}