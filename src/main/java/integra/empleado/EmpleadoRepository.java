package integra.empleado;

import integra.asistencia.query.EmpleadoModelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Integer>, EmpleadoRepositoryCustom {
    <T> List<T> findBy(Class<T> type);

    <T> Optional<T> findByPin(String clave, Class<T> type);


    @Query("select e from EmpleadoEntity e where e.codigoEmpleado = ?1")
    Optional<EmpleadoModelInfo> findByCodigo(String codigo);

    <T> Optional<T> findById(Integer integer, Class<T> type);

    List<InfoEmpleados> findByPuesto_IdAndEstatus(Integer id, String estatus);


}