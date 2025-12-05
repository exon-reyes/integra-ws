package integra.asistencia.service.kiosco;

import integra.asistencia.exception.KioscoConfigException;
import integra.globalexception.BusinessRuleException;
import integra.unidad.repository.UnidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalTime;

/**
 * Servicio que proporciona operaciones de comando para la gestión de kioscos.
 *
 * <p>Esta clase ofrece métodos para administrar diferentes aspectos de los kioscos,
 * incluyendo la configuración de uso de cámara, generación y uso de códigos de configuración,
 * y gestión de solicitudes de configuración.</p>
 *
 * <p>Ejemplo de uso:
 * <pre>
 * {@code
 * @Autowired
 * private KioscoCommandService kioscoService;
 *
 * // Actualizar uso de cámara
 * kioscoService.actualizarUsoCamara(123, true);
 *
 * // Generar código de configuración
 * String codigo = kioscoService.generarCodigoConfig(123);
 *
 * // Solicitar código de configuración
 * kioscoService.solicitarCodigo(123);
 *
 * // Usar código de configuración
 * kioscoService.usarCodigoConfig(123, "12345");
 * }
 * </pre>
 * </p>
 *
 * @author Pablo Reyes
 * @version 1.0
 * @since 1.0
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class KioscoCommandService {
    private final UnidadRepository repository;


    /**
     * Actualiza el estatus de uso de cámara para el kiosco especificado.
     *
     * @param idKiosco     Identificador único del kiosco a actualizar
     * @param nuevoEstatus Nuevo estatus de uso de cámara (true para habilitado, false para deshabilitado)
     * @throws BusinessRuleException si no se puede actualizar el kiosco
     */
    public void actualizarUsoCamara(Integer idKiosco, Boolean nuevoEstatus) {
        SecureRandom random = new SecureRandom();
        Integer version = 10000 + random.nextInt(90000);
        int registrosActualizados = repository.actualizarUsoCamara(nuevoEstatus, version, idKiosco);
        if (registrosActualizados == 0) {
            throw new BusinessRuleException("Sin modificaciones registradas", "No se pudo actualizar el kiosco");
        }
    }


    /**
     * Genera un código de configuración aleatorio para el kiosco especificado.
     *
     * @param id Identificador único del kiosco al que se asignará el código de configuración
     * @return El código de configuración generado como una cadena de 5 dígitos
     */
    public String generarCodigoConfig(Integer id) {
        String codigo = String.valueOf(10000 + new SecureRandom().nextInt(90000));
        repository.actualizarSolicitudCodigoConfig(false, id);
        repository.asignarCodigoConfig(codigo, id);
        return codigo;
    }


    /**
     * Solicita un código de configuración para el kiosco especificado.
     *
     * @param id Identificador único del kiosco que solicita el código
     * @throws KioscoConfigException si no se puede registrar la solicitud de código
     */
    public void solicitarCodigo(Integer id) {
        if (repository.actualizarSolicitudCodigoConfig(true, id) == 0) {
            throw new KioscoConfigException("Sin modificaciones registradas");
        }
    }


    /**
     * Usa un código de configuración para el kiosco especificado.
     *
     * <p>Este método verifica si el código proporcionado existe en el sistema.
     * Si el código es válido, se procede a eliminar el código de configuración
     * del kiosco especificado.</p>
     *
     * @param id     Identificador único del kiosco que usará el código
     * @param codigo Código de configuración a utilizar
     * @throws KioscoConfigException si el código no existe o no es válido
     * @see UnidadRepository#existeCodigo(String) para verificar la existencia del código
     * @see UnidadRepository#asignarCodigoConfig(String, Integer) para eliminar el código del kiosco
     */
    public void usarCodigoConfig(Integer id, String codigo) {
        if (!repository.existeCodigo(codigo)) {
            throw new KioscoConfigException("Código no encontrado");
        }
        repository.asignarCodigoConfig(null, id);
    }

    /**
     * Cancela la solicitud de código de configuración para el kiosco especificado.
     *
     * <p>Este método actualiza el estado de solicitud de código de configuración
     * a falso, indicando que ya no se requiere un código de configuración.</p>
     *
     * @param id Identificador único del kiosco para el cual se cancela la solicitud
     * @throws KioscoConfigException si no se puede actualizar el estado de la solicitud
     * @see UnidadRepository#actualizarSolicitudCodigoConfig(Boolean, Integer) para actualizar el estado
     */
    public void cancelarRequiereCodigo(Integer id) {
        if (repository.actualizarSolicitudCodigoConfig(false, id) == 0) {
            throw new KioscoConfigException("Sin modificaciones registradas");
        }
    }

    public void actualizarCompensacion(Integer id, LocalTime compensacion) {
        if (repository.actualizarCompensacionKiosco(compensacion, id) == 0) {
            throw new KioscoConfigException("Sin modificaciones registradas");
        }
    }
}
