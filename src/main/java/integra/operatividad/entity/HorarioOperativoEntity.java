package integra.operatividad.entity;

import integra.unidad.entity.UnidadEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "horario_operativo")
public class HorarioOperativoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operatividad_id", nullable = false)
    private OperatividadEntity operatividad;

    @NotNull
    @Column(name = "apertura", nullable = false)
    private LocalTime apertura;

    @NotNull
    @Column(name = "cierre", nullable = false)
    private LocalTime cierre;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_id", nullable = false)
    private UnidadEntity unidad;

}