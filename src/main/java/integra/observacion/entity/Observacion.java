package integra.observacion.entity;

import integra.auditoria.Auditoria;
import integra.departamento.entity.DepartamentoEntity;
import integra.empleado.EmpleadoEntity;
import integra.unidad.entity.UnidadEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "observacion")
public class Observacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "folio_observacion", nullable = false, length = 30)
    private String folioObservacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditoria_id")
    private Auditoria auditoria;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_id", nullable = false)
    private UnidadEntity unidad;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_observacion_id", nullable = false)
    private TipoObservacion tipoObservacion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "estatus_id", nullable = false)
    private Estatus estatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creado_por_id", nullable = false)
    private EmpleadoEntity creadoPor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_responsable_id", nullable = false)
    private DepartamentoEntity departamentoResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_origen_id")
    private DepartamentoEntity departamentoOrigen;

    @Size(max = 200)
    @NotNull
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotNull
    @Lob
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @ColumnDefault("'MEDIA'")
    @Lob
    @Column(name = "prioridad", nullable = false)
    private String prioridad;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "es_privada", nullable = false)
    private Boolean esPrivada = false;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "requiere_seguimiento", nullable = false)
    private Boolean requiereSeguimiento = false;

    @Column(name = "fecha_resolucion")
    private Instant fechaResolucion;

    @Column(name = "tiempo_resolucion_horas")
    private Integer tiempoResolucionHoras;

    @ColumnDefault("0")
    @Column(name = "total_comentarios")
    private Integer totalComentarios;

    @ColumnDefault("0")
    @Column(name = "total_archivos")
    private Integer totalArchivos;

    @ColumnDefault("0")
    @Column(name = "total_subtareas")
    private Integer totalSubtareas;

    @ColumnDefault("0")
    @Column(name = "subtareas_completadas")
    private Integer subtareasCompletadas;

    @Column(name = "creado")
    private LocalDateTime creado;

    @Column(name = "modificado")
    private LocalDateTime modificado;

}