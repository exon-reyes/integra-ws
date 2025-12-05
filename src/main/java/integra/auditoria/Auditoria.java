package integra.auditoria;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "auditoria")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "folio", nullable = false, length = 30)
    private String folio;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @NotNull
    @ColumnDefault("'PROGRAMADA'")
    @Lob
    @Column(name = "estatus", nullable = false)
    private String estatus;

    @Lob
    @Column(name = "observaciones_generales")
    private String observacionesGenerales;

    @ColumnDefault("0")
    @Column(name = "observaciones_encontradas")
    private Integer observacionesEncontradas;

    @Column(name = "calificacion_general", precision = 3, scale = 2)
    private BigDecimal calificacionGeneral;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "requiere_seguimiento", nullable = false)
    private Boolean requiereSeguimiento = false;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}