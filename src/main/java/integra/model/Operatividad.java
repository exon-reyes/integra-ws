package integra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Operatividad {
    private Integer id;
    private String nombre;

    public Operatividad(Integer id) {
        this.id = id;
    }
}
