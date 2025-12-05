package integra.asistencia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "incidencia_kiosco")
public class IncidenciaKioscoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "entidad_id", nullable = false)
    private Integer entidadId;

    @NotNull
    @Column(name = "tipo_incidencia", nullable = false, columnDefinition = "ENUM('unidad_incorrecta','fuera_horario')")
    private String tipoIncidencia;

    @Size(max = 255)
    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "empleado_id")
    private Integer empleadoId;

    @ColumnDefault("current_timestamp()")
    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Size(max = 255)
    @Column(name = "path_imagen")
    private String pathImagen;

    @Column(name = "id_esperado")
    private Integer idEsperado;

    @Column(name = "id_registrado")
    private Integer idRegistrado;

}