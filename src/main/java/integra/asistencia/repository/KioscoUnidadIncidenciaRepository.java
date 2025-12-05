package integra.asistencia.repository;

import integra.asistencia.entity.Incidencia;
import integra.asistencia.entity.IncidenciaKioscoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface KioscoUnidadIncidenciaRepository extends JpaRepository<IncidenciaKioscoEntity, Integer> {
    @Query("""
                select i from IncidenciaKioscoEntity i
                where i.fecha between ?1 and ?2
                and i.empleadoId = ?3
            """)
    List<Incidencia> findByFechaBetweenAndEmpleadoId(LocalDateTime fechaInicio, LocalDateTime fechaFin, Integer empleadoId);

}