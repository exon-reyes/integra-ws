package integra.unidad.repository;

import integra.unidad.entity.UnidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad UnidadEntity.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas
 * relacionadas con las unidades organizacionales.
 */
public interface UnidadRepository extends JpaRepository<UnidadEntity, Integer> {
    /**
     * Busca todas las entidades del tipo especificado.
     *
     * @param <T>  Tipo de la entidad a devolver
     * @param type Clase del tipo de entidad solicitada
     * @return Lista de entidades del tipo especificado
     */
    <T> List<T> findBy(Class<T> type);

    /**
     * Busca una entidad por su ID del tipo especificado.
     *
     * @param <T>     Tipo de la entidad a devolver
     * @param integer ID de la entidad a buscar
     * @param type    Clase del tipo de entidad solicitada
     * @return Optional conteniendo la entidad encontrada o vacío si no existe
     */
    <T> Optional<T> findById(Integer integer, Class<T> type);

    /**
     * Busca entidades por ID de supervisor ordenadas por clave ascendente.
     *
     * @param <T>  Tipo de la entidad a devolver
     * @param id   ID del supervisor
     * @param type Clase del tipo de entidad solicitada
     * @return Lista de entidades del tipo especificado ordenadas por clave
     */
    <T> List<T> findBySupervisor_IdOrderByClaveAsc(Integer id, Class<T> type);

    /**
     * Actualiza el estado activo de una unidad por su ID.
     *
     * @param activo Nuevo valor del estado activo
     * @param id     ID de la unidad a actualizar
     */
    @Modifying
    @Query("update UnidadEntity u set u.activo = ?1 where u.id = ?2")
    void updateActivoById(Boolean activo, Integer id);

    /**
     * Verifica si existe una unidad con el email especificado.
     *
     * @param email Email a verificar
     * @return true si existe una unidad con ese email, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe una unidad con el nombre especificado.
     *
     * @param nombre Nombre a verificar
     * @return true si existe una unidad con ese nombre, false en caso contrario
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si existe una unidad con la clave especificada.
     *
     * @param clave Clave a verificar
     * @return true si existe una unidad con esa clave, false en caso contrario
     */
    boolean existsByClave(String clave);

    /**
     * Verifica si existe una unidad con el teléfono especificado.
     *
     * @param telefono Teléfono a verificar
     * @return true si existe una unidad con ese teléfono, false en caso contrario
     */
    boolean existsByTelefono(String telefono);

    /**
     * Busca todas las entidades activas del tipo especificado.
     *
     * @param <T>  Tipo de la entidad a devolver
     * @param type Clase del tipo de entidad solicitada
     * @return Lista de entidades activas del tipo especificado
     */
    <T> List<T> findByActivoTrue(Class<T> type);

    /**
     * Elimina una unidad por su ID.
     *
     * @param id ID de la unidad a eliminar
     */
    @Modifying
    @Query("DELETE FROM UnidadEntity u WHERE u.id = ?1")
    void deleteById(Integer id);

    /**
     * Actualiza el uso de cámara y la versión del kiosco para una unidad específica.
     *
     * @param requiereCamara Indica si la unidad requiere cámara (true/false)
     * @param versionKiosco  La versión del kiosco a asignar
     * @param id             El identificador de la unidad
     * @return El número de filas afectadas por la actualización
     */
    @Modifying
    @Query("update UnidadEntity u set u.requiereCamara = ?1, u.versionKiosco = ?2 where u.id = ?3")
    int actualizarUsoCamara(Boolean requiereCamara, Integer versionKiosco, Integer id);

    /**
     * Asigna un código de autorización al kiosco de una unidad específica.
     *
     * @param codigoResetKiosco El código de autorización a asignar. Puede ser null para eliminar el código existente.
     * @param id                El identificador de la unidad
     * @Nota: Este método también puede utilizarse para usar el código existente en escenarios donde
     * se consulta el código de autorización. En caso de que ya exista un código, este puede ser
     * sobrescrito con null para aprobar al usuario y permitirle el acceso.
     */
    @Transactional
    @Modifying
    @Query("update UnidadEntity u set u.codigoAutorizacionKiosco = ?1 where u.id = ?2")
    void asignarCodigoConfig(String codigoResetKiosco, Integer id);

    /**
     * Verifica si existe una unidad con el código de autorización de kiosco especificado.
     *
     * @param codigoResetKiosco El código de autorización de kiosco a buscar
     * @return true si existe una unidad con el código especificado, false en caso contrario
     */
    @Query("select (count(u) > 0) from UnidadEntity u where u.codigoAutorizacionKiosco = ?1")
    boolean existeCodigo(String codigoResetKiosco);

    /**
     * Agrega o actualiza el estado de solicitud de código de autorización para el kiosco.
     * Este método puede ser usado por el cliente kiosco para solicitar un código al administrador,
     * o por el administrador para aceptar/rechazar la solicitud.
     *
     * @param requiereCodigo Indica si se requiere generar un código (true) o se rechaza la solicitud (false)
     * @param id             El identificador de la unidad
     * @return El número de filas afectadas por la actualización
     */
    @Transactional
    @Modifying
    @Query("update UnidadEntity u set u.requiereCodigo = ?1 where u.id = ?2")
    int actualizarSolicitudCodigoConfig(Boolean requiereCodigo, Integer id);

    @Transactional
    @Modifying
    @Query("update UnidadEntity u set u.tiempoCompensacion = ?1 where u.id = ?2")
    int actualizarCompensacionKiosco(LocalTime tiempoCompensacion, Integer id);


}