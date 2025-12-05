package integra.asistencia.entity;

import integra.empleado.EmpleadoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "asistencia")
public class AsistenciaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoEntity empleado;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "inicio_jornada")
    private LocalDateTime inicioJornada;

    @Column(name = "fin_jornada")
    private LocalDateTime finJornada;

    @ColumnDefault("0")
    @Column(name = "jornada_cerrada")
    private Boolean jornadaCerrada;


    @Size(max = 100)
    @Column(name = "path_foto_inicio", length = 100)
    private String pathFotoInicio;

    @Size(max = 100)
    @Column(name = "path_foto_fin", length = 100)
    private String pathFotoFin;


    @Size(max = 100)
    @Column(name = "comentario", length = 100)
    private String comentario;

    @ColumnDefault("0")
    @Column(name = "cerrado_automatico")
    private Boolean cerradoAutomatico;

    @OneToMany(mappedBy = "asistencia", fetch = FetchType.LAZY)
    private List<PausaModel> pausas;

    @ColumnDefault("0")
    @Column(name = "inconsistencia")
    private Boolean inconsistencia;

    @Column(name = "tiempo_compensado")
    private LocalTime tiempoCompensado;

    public AsistenciaModel() {
    }

    public AsistenciaModel(Integer id) {
        this.id = id;
    }
}