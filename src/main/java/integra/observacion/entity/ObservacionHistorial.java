package integra.observacion.entity;

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
@Table(name = "observacion_historial")
public class ObservacionHistorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "observacion_id", nullable = false)
    private Integer observacionId;

    @NotNull
    @Lob
    @Column(name = "accion", nullable = false)
    private String accion;

    @Lob
    @Column(name = "detalle_cambio")
    private String detalleCambio;

    @ColumnDefault("current_timestamp()")
    @Column(name = "creado", insertable = false, updatable = false)
    private LocalDateTime creado;

    @Size(max = 30)
    @Column(name = "usuario", length = 30)
    private String usuario;

    public ObservacionHistorial(Integer observacionId, String accion, String detalleCambio, String usuario) {
        this.observacionId = observacionId;
        this.accion = accion;
        this.detalleCambio = detalleCambio;
        this.usuario = usuario;
    }

    public ObservacionHistorial() {
    }
}