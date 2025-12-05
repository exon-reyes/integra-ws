package integra.asistencia.entity;

import lombok.Getter;

@Getter
public enum TipoIncidencia {
    UNIDAD_INCORRECTA("unidad_incorrecta"), FUERA_HORARIO("fuera_horario"), SALIDA_ANTICIPADA("salida_anticipada");

    private final String value;

    TipoIncidencia(String value) {
        this.value = value;
    }

    public static TipoIncidencia fromValue(String value) {
        for (TipoIncidencia tipo : values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de incidencia desconocido: " + value);
    }
}