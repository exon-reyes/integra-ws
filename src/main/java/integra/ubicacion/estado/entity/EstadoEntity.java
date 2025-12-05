package integra.ubicacion.estado.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estado")
public class EstadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 8)
    @NotNull
    @Column(name = "codigo", nullable = false, length = 8)
    private String codigo;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;


    public EstadoEntity() {
    }

    public EstadoEntity(Integer id) {
        this.id = id;
    }
}