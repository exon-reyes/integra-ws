package integra.asistencia.model;

import java.util.concurrent.ConcurrentHashMap;

public record TiempoTrabajo(int minutos) {
    private static final ConcurrentHashMap<Integer, TiempoTrabajo> CACHE = new ConcurrentHashMap<>();
    private static final TiempoTrabajo CERO = new TiempoTrabajo(0);

    public TiempoTrabajo {
        if (minutos < 0) {
            throw new IllegalArgumentException("Los minutos no pueden ser negativos");
        }
    }

    public static TiempoTrabajo cero() {
        return CERO;
    }

    /**
     * Crea y devuelve un objeto TiempoTrabajo basado en la cantidad de horas proporcionadas.
     * Este método convierte las horas en minutos y utiliza un mecanismo de caché para
     * optimizar el rendimiento, devolviendo una instancia existente si ya está creada.
     *
     * @param horas La cantidad de horas utilizadas para crear el objeto TiempoTrabajo.
     *              Debe ser un valor no negativo.
     * @return Un objeto TiempoTrabajo correspondiente a las horas especificadas convertidas en minutos.
     * @throws IllegalArgumentException Si se proporciona un valor negativo para las horas.
     */
    public static TiempoTrabajo deHoras(int horas) {
        // Convierte las horas en minutos, que es la unidad interna utilizada por TiempoTrabajo
        int minutos = horas * 60;
        // Usa el caché para evitar crear múltiples instancias innecesarias para el mismo valor
        return CACHE.computeIfAbsent(minutos,
                TiempoTrabajo::new);
    }

    /**
     * Crea y devuelve un objeto TiempoTrabajo basado en la cantidad de minutos proporcionados.
     * Este método utiliza un mecanismo de caché para optimizar el rendimiento,
     * devolviendo una instancia existente si ya está creada. Si los minutos son menores o iguales a cero,
     * se devuelve una instancia predefinida representando cero minutos.
     *
     * @param minutos La cantidad de minutos utilizados para crear el objeto TiempoTrabajo.
     *                Debe ser un valor no negativo.
     * @return Un objeto TiempoTrabajo correspondiente a los minutos especificados.
     * @throws IllegalArgumentException Si se proporciona un valor negativo para los minutos.
     */
    public static TiempoTrabajo deMinutos(int minutos) {
        if (minutos <= 0) return CERO;
        return CACHE.computeIfAbsent(minutos,
                TiempoTrabajo::new);
    }


    /**
     * Este método resta el tiempo representado por otro objeto TiempoTrabajo del tiempo actual.
     * Utiliza la función estática deMinutos para asegurarse de que el resultado sea un objeto TiempoTrabajo válido.
     * La función Math.max se utiliza para garantizar que el resultado no sea negativo, ya que el tiempo no puede ser negativo.
     */
    public TiempoTrabajo restar(TiempoTrabajo otro) {
        return deMinutos(Math.max(0,
                this.minutos - otro.minutos));
    }


    // === COMPARACIONES ===

    /**
     * Compara si el tiempo actual es mayor que el tiempo proporcionado.
     *
     * @param otro El objeto TiempoTrabajo con el que se va a comparar.
     * @return true si el tiempo actual es mayor que el tiempo proporcionado, false en caso contrario.
     * @example <pre>
     * TiempoTrabajo tiempo1 = TiempoTrabajo.deMinutos(120);
     * TiempoTrabajo tiempo2 = TiempoTrabajo.deMinutos(60);
     * boolean esMayor = tiempo1.esMayorQue(tiempo2); // esMayor será true
     * </pre>
     */
    public boolean esMayorQue(TiempoTrabajo otro) {
        return this.minutos > otro.minutos;
    }

    /**
     * Compara los minutos de este objeto TiempoTrabajo con otro objeto TiempoTrabajo.
     *
     * @param otro el objeto TiempoTrabajo con el que se va a comparar
     * @return true si los minutos de este objeto son menores que los minutos del objeto 'otro'; false en caso contrario
     */
    public boolean esMenorQue(TiempoTrabajo otro) {
        return this.minutos < otro.minutos;
    }


    /**
     * Obtiene las horas representadas por el objeto TiempoTrabajo.
     * Este método divide los minutos almacenados por 60 para obtener la cantidad de horas.
     *
     * @return La cantidad de horas representadas por el objeto TiempoTrabajo.
     * @example <pre>
     * TiempoTrabajo tiempo = TiempoTrabajo.deMinutos(120);
     * int horas = tiempo.getHoras(); // horas será 2
     * </pre>
     */
    public int getHoras() {
        return minutos / 60;
    }


    /**
     * Obtiene minutos restantes
     * <p>
     * Este método se utiliza para calcular los minutos restantes en una hora. Utiliza el operador de módulo para obtener el
     * valor de los minutos que no alcanzan una hora completa a partir del valor total de los minutos almacenados en la
     * variable 'minutos'.
     *
     * @return int Los minutos restantes, siempre será un valor entre 0 y 59.
     */
    public int getMinutosRestantes() {
        return minutos % 60;
    }

}