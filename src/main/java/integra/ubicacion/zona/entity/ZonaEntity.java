package integra.ubicacion.zona.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@Entity
@Table(name = "zona")
@DynamicInsert
public class ZonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;

    public ZonaEntity(Integer id) {
        this.id = id;
    }

    public ZonaEntity() {
    }

    public ZonaEntity(String nombre) {
        this.nombre = nombre;
    }
}