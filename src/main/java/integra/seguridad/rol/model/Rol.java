package integra.seguridad.rol.model;

import integra.model.Permiso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Rol {
    private Long id;
    private String nombre;
    private String descripcion;
    private Set<Permiso> permisos;
    private Boolean activo;
    private Boolean rolDefault;

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public Rol(Long id, String nombre, String descripcion, Set<Permiso> permisos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.permisos = permisos;
    }

    public Rol(Long id, String nombre, String descripcion, Boolean rolDefault) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rolDefault = rolDefault;
    }

    public Rol(Long id, String name, String description) {
        this.id = id;
        this.nombre = name;
        this.descripcion = description;
    }

    public void setPermisosList(Set<String> nombre) {
        if (this.permisos == null) {
            this.permisos = new HashSet<>();
        }
        nombre.forEach(data -> {
            this.permisos.add(new Permiso(data));
        });
    }

}
