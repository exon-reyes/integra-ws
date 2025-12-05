package integra.observacion.entity;

import integra.departamento.entity.DepartamentoEntity;
import integra.empleado.EmpleadoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "observacion_colaboraciones")
public class ObservacionColaboracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "observacion_id", nullable = false)
    private Observacion observacion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_id", nullable = false)
    private DepartamentoEntity departamento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignado_por_id", nullable = false)
    private EmpleadoEntity asignadoPor;

    @ColumnDefault("current_timestamp()")
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "activa", nullable = false)
    private Boolean activa = false;

}