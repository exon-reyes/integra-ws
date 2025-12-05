package integra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Zona {
    private Integer id;
    private String nombre;

    public Zona(String nombre) {
        this.nombre = nombre;
    }
}
