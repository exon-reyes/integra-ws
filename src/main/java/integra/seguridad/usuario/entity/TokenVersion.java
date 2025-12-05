package integra.seguridad.usuario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "token_version")
public class TokenVersion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "version", nullable = false)
    private Integer version;

    public TokenVersion(Integer id, String username, Integer version) {
        this.id = id;
        this.username = username;
        this.version = version;
    }

    public TokenVersion() {
    }
}