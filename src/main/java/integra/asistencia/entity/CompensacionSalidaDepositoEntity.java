package integra.asistencia.entity;

import integra.empleado.EmpleadoEntity;
import integra.unidad.entity.UnidadEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "compensacion_salida_deposito")
@DynamicUpdate
@DynamicInsert
public class CompensacionSalidaDepositoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asistencia_id", nullable = false)
    private AsistenciaModel asistencia;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private EmpleadoEntity empleado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_id", nullable = false)
    private UnidadEntity unidad;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "hora_salida", nullable = false)
    private LocalTime horaSalida;

    @NotNull
    @Column(name = "horas_trabajadas", nullable = false)
    private LocalTime horasTrabajadas;

    @NotNull
    @Column(name = "horas_faltantes", nullable = false)
    private LocalTime horasFaltantes;

    @NotNull
    @Column(name = "tiempo_compensado", nullable = false)
    private LocalTime tiempoCompensado;


}