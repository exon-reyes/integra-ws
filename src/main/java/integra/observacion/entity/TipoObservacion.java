package integra.observacion.entity;

import integra.departamento.entity.DepartamentoEntity;
import integra.reportes.CategoriaObservacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "tipo_observacion")
public class TipoObservacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;

    @ColumnDefault("'BAJA'")
    @Lob
    @Column(name = "prioridad_default")
    private String prioridadDefault;

    @ColumnDefault("72")
    @Column(name = "sla_horas")
    private Short slaHoras;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "activo", nullable = false)
    private Boolean activo = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_responsable_id")
    private DepartamentoEntity departamentoResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaObservacion categoria;

}