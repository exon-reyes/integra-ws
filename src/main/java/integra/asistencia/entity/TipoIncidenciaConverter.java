package integra.asistencia.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoIncidenciaConverter implements AttributeConverter<TipoIncidencia, String> {

    @Override
    public String convertToDatabaseColumn(TipoIncidencia tipoIncidencia) {
        return tipoIncidencia != null ? tipoIncidencia.getValue() : null;
    }

    @Override
    public TipoIncidencia convertToEntityAttribute(String value) {
        return value != null ? TipoIncidencia.fromValue(value) : null;
    }
}