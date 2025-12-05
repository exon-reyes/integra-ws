package integra.asistencia.util;


import integra.asistencia.model.TiempoTrabajo;

public class JornadaUtil {
    public static String formatearDiferencia(TiempoTrabajo tiempo, boolean esPositivo) {
        if (tiempo.minutos() == 0) {
            return "";
        }

        String signo = esPositivo ? "+" : "-";
        int horas = tiempo.getHoras();
        int minutos = tiempo.getMinutosRestantes();

        // Si la diferencia es menor a 1 hora, solo mostrar minutos
        if (horas == 0) {
            return String.format("%s%d min",
                    signo,
                    minutos);
        }

        // Si hay horas y no hay minutos
        if (minutos == 0) {
            return String.format("%s%d h",
                    signo,
                    horas);
        } else {
            // Si hay horas y minutos
            return String.format("%s%d h %02d min",
                    signo,
                    horas,
                    minutos);
        }
    }

    public static String formatearTiempoHorasMinutos(TiempoTrabajo tiempo) {
        if (tiempo.minutos() == 0) {
            return "00:00 HR";
        }
        int horas = tiempo.getHoras();
        int minutos = tiempo.getMinutosRestantes();
        return String.format("%02d:%02d HR",
                horas,
                minutos);
    }
}
