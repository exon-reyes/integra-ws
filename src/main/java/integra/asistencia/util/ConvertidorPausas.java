package integra.asistencia.util;

import integra.asistencia.entity.PausaModel;
import integra.asistencia.model.PausaAsistencia;

import java.time.LocalTime;
import java.util.List;

/**
 * Utilería para conversiones y operaciones con pausas
 */
public class ConvertidorPausas {

    /**
     * Convierte una lista de PausaModel a PausaAsistencia
     */
    public static List<PausaAsistencia> mapearPausas(List<?> pausasEntity) {
        if (pausasEntity == null || pausasEntity.isEmpty()) {
            return List.of();
        }

        return pausasEntity.stream().map(p -> {
            var pausa = (PausaModel) p;
            PausaAsistencia pausaAsistencia = new PausaAsistencia(pausa.getId());
            pausaAsistencia.setTipoPausa(TipoPausa.valueOf(pausa.getTipo()));
            pausaAsistencia.setInicio(pausa.getInicio());
            pausaAsistencia.setFin(pausa.getFin());
            return pausaAsistencia;
        }).toList();
    }

    /**
     * Convierte minutos a LocalTime (hh:mm)
     */
    public static LocalTime convertirMinutosALocalTime(int minutos) {
        if (minutos < 0) {
            minutos = 0;
        }
        int horas = minutos / 60;
        int minutosRestantes = minutos % 60;
        return LocalTime.of(horas, minutosRestantes);
    }
}