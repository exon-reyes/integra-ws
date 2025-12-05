package integra.observacion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "estatus")
public class Estatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;

    @Size(max = 10)
    @Column(name = "color_bg", length = 10)
    private String colorBg;

    @Size(max = 25)
    @Column(name = "nombre_icono", length = 25)
    private String nombreIcono;

    @ColumnDefault("1")
    @Column(name = "es_inicial")
    private Boolean esInicial;

    @ColumnDefault("0")
    @Column(name = "es_final")
    private Boolean esFinal;

    public Estatus(Integer id) {
        this.id = id;
    }

    public Estatus() {
    }
}