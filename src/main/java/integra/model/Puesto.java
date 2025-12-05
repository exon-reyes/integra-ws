package integra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Setter
public class Puesto implements Serializable {
    private Integer id;
    private String nombre;
    private Boolean activo;

    public Puesto(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}