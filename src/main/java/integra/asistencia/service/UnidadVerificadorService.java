package integra.asistencia.service;

import integra.asistencia.entity.IncidenciaKioscoEntity;
import integra.asistencia.entity.TipoIncidencia;
import integra.asistencia.query.UnidadKioscoIncidencia;
import integra.asistencia.repository.KioscoUnidadIncidenciaRepository;
import integra.unidad.repository.UnidadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio para la verificación y registro de incidencias
 * asociadas al uso de quioscos de asistencia (Kiosco).
 * <p>
 * Incluye métodos para registrar asincrónicamente eventos de incidencia en la base de datos,
 * manejando casos especiales como la detección de registro en una unidad incorrecta.
 * </p>
 *
 * @author Pablo Reyes
 * @since 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UnidadVerificadorService {

    private final KioscoUnidadIncidenciaRepository repository;
    private final UnidadRepository unidadRepository;

    /**
     * Registra una incidencia generada por el uso del quiosco de manera asíncrona.
     * <p>
     * Este método se ejecuta en un hilo separado (pool "UnidadRegistroKiosco") para no bloquear
     * el flujo principal de la aplicación. Se asegura la atomicidad de la operación mediante {@code @Transactional}.
     * </p>
     *
     * @param entidadId        El ID de la entidad o sucursal a la que pertenece el empleado.
     * @param empleadoId       El ID del empleado que generó la incidencia.
     * @param idUnidadEsperada El ID de la unidad (ej. ubicación) donde se esperaba el registro.
     * @param idUnidadRegistro El ID de la unidad donde realmente se efectuó el registro.
     * @param imagen           El path (ruta) de la imagen de respaldo o evidencia de la incidencia.
     * @param tipoIncidencia   El tipo de incidencia detectada (ej. {@link TipoIncidencia#UNIDAD_INCORRECTA}).
     * @param tipoRegistro     La descripción del tipo de registro (ej. "Entrada", "Salida").
     */
    @Async("UnidadRegistroKiosco")
    @Transactional
    public void registrarIncidenciaKioscoAsync(Integer entidadId, Integer empleadoId, Integer idUnidadEsperada, Integer idUnidadRegistro, String imagen, TipoIncidencia tipoIncidencia, String tipoRegistro) {
        IncidenciaKioscoEntity incidencia = new IncidenciaKioscoEntity();

        // Lógica específica para INCIDENCIA de UNIDAD INCORRECTA
        if (tipoIncidencia == TipoIncidencia.UNIDAD_INCORRECTA) {
            Optional<UnidadKioscoIncidencia> unidadEsperadaRegistro = unidadRepository.findById(idUnidadEsperada, UnidadKioscoIncidencia.class);
            Optional<UnidadKioscoIncidencia> unidadRealRegistro = unidadRepository.findById(idUnidadRegistro, UnidadKioscoIncidencia.class);

            // Se asume que los Optional contienen valores. Si no es así, podría lanzar una NoSuchElementException.
            String unidadEsperada = unidadEsperadaRegistro.map(UnidadKioscoIncidencia::nombreCompleto)
                    .orElse("ID Desconocido: " + idUnidadEsperada);
            String unidadRegistrada = unidadRealRegistro.map(UnidadKioscoIncidencia::nombreCompleto)
                    .orElse("ID Desconocido: " + idUnidadRegistro);

            incidencia.setMensaje(tipoRegistro + ": Se esperaba registro en la unidad " + unidadEsperada + ", pero se efectuó en " + unidadRegistrada);
        }

        incidencia.setEntidadId(entidadId);
        incidencia.setTipoIncidencia(tipoIncidencia.getValue());
        incidencia.setIdEsperado(idUnidadEsperada);
        incidencia.setIdRegistrado(idUnidadRegistro);
        incidencia.setEmpleadoId(empleadoId);
        incidencia.setFecha(LocalDateTime.now());
        incidencia.setPathImagen(imagen);

        repository.save(incidencia);
        log.info("⚙️ Incidencia Kiosco registrada async. EmpID: {}, Tipo: {}", empleadoId, tipoIncidencia);
    }
}