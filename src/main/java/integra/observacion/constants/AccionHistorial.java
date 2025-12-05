package integra.observacion.constants;

public enum AccionHistorial {
    CREADA("Observación creada"),
    MODIFICADA("Observación modificada"),
    ESTADO_CAMBIADO("Estado cambiado"),
    COMENTARIO_AGREGADO("Comentario agregado"),
    ARCHIVO_ADJUNTADO("Archivo adjuntado"),
    SUBTAREA_CREADA("Subtarea creada"),
    SUBTAREA_COMPLETADA("Subtarea completada"),
    REASIGNADA("Observación reasignada"),
    CERRADA("Observación cerrada");

    private final String descripcion;

    AccionHistorial(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}