package integra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Permiso {
    private String id;
    private String nombre;

    public Permiso(String id) {
        this.id = id;
    }
}
