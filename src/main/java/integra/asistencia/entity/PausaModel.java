package integra.asistencia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pausa")
public class PausaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asistencia", nullable = false)
    private AsistenciaModel asistencia;

    @Size(max = 30)
    @NotNull
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @NotNull
    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fin")
    private LocalDateTime fin;

    @Size(max = 100)
    @Column(name = "path_foto_inicio", length = 100)
    private String pathFotoInicio;

    @Size(max = 100)
    @Column(name = "path_foto_fin", length = 100)
    private String pathFotoFin;


}