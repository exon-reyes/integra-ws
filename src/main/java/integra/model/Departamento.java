package integra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Departamento {
    private Integer id;
    private String nombre;

    public Departamento(Integer id) {
        this.id = id;
    }

    public Departamento(String nombre) {
        this.nombre = nombre;
    }
}