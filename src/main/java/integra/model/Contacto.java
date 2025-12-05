package integra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contacto {
    private String direccion;
    private String telefono;
    private String email;
    private Estado estado;
    private String localizacion;
    private Zona zona;

    public Contacto(Zona zona) {
        this.zona = zona;
    }

    public Contacto(String telefono, String email, Estado estado, String localizacion, Zona zona) {
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
        this.localizacion = localizacion;
        this.zona = zona;
    }
}
