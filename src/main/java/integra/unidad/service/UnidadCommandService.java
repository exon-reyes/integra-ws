package integra.unidad.service;

import integra.globalexception.DuplicateDataException;
import integra.globalexception.FKDataException;
import integra.globalexception.InvalidReferenceException;
import integra.unidad.entity.UnidadEntity;
import integra.unidad.repository.UnidadRepository;
import integra.unidad.request.ActualizarUnidad;
import integra.unidad.request.NuevaUnidad;
import integra.unidad.validator.IUnidadValidator;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnidadCommandService {

    private final UnidadRepository writeRepository;
    private final IUnidadValidator validator;

    // --- REGISTRAR ---
    @Transactional
    @CacheEvict(value = {"unidadList", "unidadesActivas", "unidadContacto", "unidadInfo"}, allEntries = true)
    public void registrarUnidad(NuevaUnidad params) {
        validator.checkExisteClaveUnidad(params.getClave());
        validator.checkExisteTelefono(params.getTelefono());
        validator.checkExisteNombre(params.getNombre());
        validator.checkExisteEmail(params.getEmail());

        UnidadEntity unidad = new UnidadEntity(params);

        try {
            writeRepository.saveAndFlush(unidad);
        } catch (DataIntegrityViolationException ex) {
            log.error("Violación de integridad al registrar unidad: {}", ex.getMessage(), ex);

            String message = ex.getMostSpecificCause().getMessage();
            if (message == null) throw ex;

            if (message.contains("FK_unidad_zona")) {
                throw new InvalidReferenceException(
                        "El ID de zona '" + params.getIdZona() + "' no existe o no es válido.",
                        "validar_id_zona",
                        "gestion_de_unidades",
                        "AUDIT_002"
                );
            }

            if (message.contains("FK_unidad_estado")) {
                throw new InvalidReferenceException(
                        "El ID de estado '" + params.getIdEstado() + "' no existe o no es válido.",
                        "validar_id_estado",
                        "gestion_de_unidades",
                        "AUDIT_003"
                );
            }

            verificarDuplicado(ex, params);
            throw ex;
        }
    }

    // --- ACTUALIZAR ---
    @Transactional
    @CacheEvict(value = {"unidadList", "unidadesActivas", "unidadContacto", "unidadInfo"}, allEntries = true)
    public void actualizarUnidad(ActualizarUnidad params) {
        UnidadEntity unidadToUpdate = new UnidadEntity(params);

        try {
            writeRepository.saveAndFlush(unidadToUpdate);
        } catch (DataIntegrityViolationException | PersistenceException ex) {
            log.error("Error al actualizar unidad ({}): {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            verificarDuplicado(ex, params);
            throw ex;
        } catch (Exception ex) {
            log.error("Excepción general al actualizar unidad: {} - {}", ex.getClass()
                    .getSimpleName(), ex.getMessage(), ex);
            Throwable cause = ex;
            while (cause != null) {
                if (verificarDuplicado(cause, params)) break;
                cause = cause.getCause();
            }
            throw ex;
        }
    }

    // --- CAMBIO DE ESTATUS ---
    @Transactional
    @CacheEvict(value = {"unidadList", "unidadesActivas", "unidadContacto", "unidadInfo"}, allEntries = true)
    public void actualizarEstatusUnidad(Integer id, Boolean estatus) {
        writeRepository.updateActivoById(estatus, id);
        log.info("Unidad {}: {}", estatus ? "habilitada" : "deshabilitada", id);
    }

    // --- ELIMINAR ---
    @Transactional
    @CacheEvict(value = {"unidadList", "unidadesActivas", "unidadContacto", "unidadInfo"}, allEntries = true)
    public void eliminarUnidad(int id) {
        try {
            writeRepository.deleteById(id);
            log.info("Unidad eliminada correctamente: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Error al eliminar unidad {}: {}", id, e.getMessage());
            throw new FKDataException("No se puede eliminar la unidad porque tiene registros asociados (por ejemplo, horarios o reportes).");
        }
    }

    // --- DETECTOR DE DUPLICADOS ---
    private boolean verificarDuplicado(Throwable ex, NuevaUnidad data) {
        String message = ex.getMessage();
        if (message == null) return false;

        if (message.contains("idx_clave_unidad") && message.contains("Duplicate entry")) {
            throw new DuplicateDataException("La clave '" + data.getClave() + "' ya está registrada en otra unidad.", "Clave duplicada");
        }
        if (message.contains("idx_unidad_email") && message.contains("Duplicate entry")) {
            throw new DuplicateDataException("El correo electrónico '" + data.getEmail() + "' ya está registrado en otra unidad.", "Correo duplicado");
        }
        if (message.contains("idx_nombre_unidad") && message.contains("Duplicate entry")) {
            throw new DuplicateDataException("El nombre '" + data.getNombre() + "' ya existe. Use un nombre diferente.", "Nombre duplicado");
        }
        if (message.contains("idx_telefono") && message.contains("Duplicate entry")) {
            throw new DuplicateDataException("El teléfono '" + data.getTelefono() + "' ya está asignado a otra unidad.", "Teléfono duplicado");
        }
        if (message.contains("idx_codigo_autorizacion_kiosco") && message.contains("Duplicate entry")) {
            throw new DuplicateDataException("El código de autorización ya está en uso.", "Código duplicado");
        }
        if (message.contains("email") && message.contains("Duplicate entry")) {
            throw new DuplicateDataException("El correo electrónico '" + data.getEmail() + "' ya está registrado.", "Correo duplicado");
        }
        return false;
    }
}
