package integra.empleado;

import integra.departamento.entity.DepartamentoEntity;
import integra.organizacion.puesto.entity.PuestoEntity;
import integra.unidad.entity.UnidadEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "empleado")
public class EmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "codigo_empleado", nullable = false, length = 20)
    private String codigoEmpleado;

    @Size(max = 10)
    @Column(name = "pin", length = 10)
    private String pin;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Size(max = 50)
    @NotNull
    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;

    @Size(max = 50)
    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 15)
    @Column(name = "telefono", length = 15)
    private String telefono;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_id", nullable = false)
    private DepartamentoEntity departamento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "puesto_id", nullable = false)
    private PuestoEntity puesto;


    @Column(name = "zona_principal_id")
    private Integer zonaPrincipal;

    @NotNull
    @ColumnDefault("'A'")
    @Lob
    @Column(name = "estatus", nullable = false)
    private String estatus;

    @NotNull
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @Lob
    @Column(name = "sexo")
    private String sexo;

    @Size(max = 152)
    @ColumnDefault("concat(`nombre`, ' ', `apellido_paterno`, ifnull(concat(' ', `apellido_materno`), ''))")
    @Column(name = "nombre_completo", length = 152)
    private String nombreCompleto;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id")
    private UnidadEntity unidad;

    public EmpleadoEntity(Integer id) {
        this.id = id;
    }

    public EmpleadoEntity() {
    }
}