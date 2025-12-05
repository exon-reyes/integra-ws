package integra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Empleado {
    private Integer id;
    private String codigo;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompleto;

    public Empleado(Integer id, String codigo, String nombreCompleto) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.codigo = codigo;
    }

    public Empleado(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Empleado(Integer id, String codigo, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }
}
