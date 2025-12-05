package integra.observacion.constants;

import lombok.Getter;

@Getter
public enum EstatusIncidente {
    ABIERTO(1, "Abierto"),
    CERRADO(2, "Cerrado"),
    RESUELTO(3, "Resuelto"),
    PENDIENTE(4, "Pendiente"),
    CANCELADO(5, "Cancelado");

    private final int id;
    private final String nombre;

    EstatusIncidente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Método estático para obtener el EstatusIncidente por su ID.
     *
     * @param id El ID del estatus.
     * @return El EstatusIncidente correspondiente.
     * @throws IllegalArgumentException si el ID no se encuentra.
     */
    public static EstatusIncidente fromId(int id) {
        for (EstatusIncidente estatus : EstatusIncidente.values()) {
            if (estatus.getId() == id) {
                return estatus;
            }
        }
        throw new IllegalArgumentException("ID de estatus no válido: " + id);
    }
}