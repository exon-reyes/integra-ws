package integra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@Setter
public final class Estado {
    private Integer id;
    private String codigo;
    private String nombre;
    private List<Zona> zonas;

    public Estado(Integer id) {
        this.id = id;
    }

    public Estado(Integer id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Estado(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
