package integra.asistencia.service;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.factory.ExcelEstiloFactory;
import integra.asistencia.model.Asistencia;
import integra.asistencia.model.EmpleadoReporte;
import integra.asistencia.model.PausaAsistencia;
import integra.asistencia.model.TiempoTrabajo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static integra.asistencia.util.JornadaUtil.formatearTiempoHorasMinutos;

/**
 * Servicio para generar reportes de asistencia en formato Excel.
 * Utiliza EmpleadoReporte como datasource.
 */
@Service
public class ExportarAsistenciaExcelService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATO_HORA_AM_PM = DateTimeFormatter.ofPattern("hh:mm:ss a", new Locale("es", "ES"));
    private static final DecimalFormat FORMATO_DECIMAL = new DecimalFormat("#0.0#");

    // Encabezados tomados de ReporteDetalladoExcelService
    private static final String[] ENCABEZADOS_PRINCIPALES = {"FECHA",
            "CLAVE",
            "NOMBRE",
            "PUESTO",
            "UNIDAD",
            "INICIO JORNADA",
            "HORA",
            "FIN JORNADA",
            "HORA",
            "TRABAJADAS",
            "T. NETAS",
            "T. PAUSA COMIDA",
            "T. OTRAS PAUSAS",
            "T. PAUSAS",
            "T. EXTRAS BRUTAS",
            "T. EXTRAS NETAS",
            "DIFERENCIA"};

    private static final String[] ENCABEZADOS_PAUSAS = {"FECHA", "TIPO PAUSA", "INICIO", "FIN", "DURACIÓN"};

    private static final String[] ENCABEZADOS_RESUMEN = {"CLAVE",
            "NOMBRE COMPLETO",
            "PUESTO",
            "UNIDAD",
            "TOTAL HORAS NETAS",
            "TOTAL HORAS EXTRAS"};

    private static final int INDENTACION_PAUSAS = 12;

    /**
     * Genera un reporte de asistencias en formato Excel usando EmpleadoReporte como datasource.
     */
    public byte[] generarReporteAsistenciaExcel(List<EmpleadoReporte> dataSource, EmpleadoReporteCommand command) throws IOException {
        try (Workbook libroTrabajo = new XSSFWorkbook(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            Sheet hoja = libroTrabajo.createSheet("Reporte Asistencias");
            ExcelEstiloFactory estilos = new ExcelEstiloFactory(libroTrabajo);

            int indiceFila = 0;
            indiceFila = crearEncabezadoGeneral(hoja, indiceFila, command, estilos);

            Row filaEncabezado = hoja.createRow(indiceFila++);
            for (int i = 0; i < ENCABEZADOS_PRINCIPALES.length; i++) {
                crearCeldaYAplicarEstilo(filaEncabezado, i, ENCABEZADOS_PRINCIPALES[i], estilos.estiloEncabezado);
            }

            for (EmpleadoReporte empleadoReporte : dataSource) {
                for (Asistencia asistencia : empleadoReporte.getAsistencias()) {
                    indiceFila = crearBloqueDeAsistencia(hoja, indiceFila, empleadoReporte, asistencia, estilos);
                }
            }

            for (int i = 0; i < ENCABEZADOS_PRINCIPALES.length; i++) {
                hoja.autoSizeColumn(i);
            }

            // Crear hoja de resumen
            crearHojaResumen(libroTrabajo, dataSource, command, estilos);

            libroTrabajo.write(salida);
            return salida.toByteArray();
        }
    }

    private int crearEncabezadoGeneral(Sheet hoja, int indiceFila, EmpleadoReporteCommand command, ExcelEstiloFactory estilos) {
        Row filaTitulo = hoja.createRow(indiceFila++);
        crearCeldaYAplicarEstilo(filaTitulo, 0, "REPORTE DE ASISTENCIAS POR EMPLEADO", estilos.estiloTitulo);
        hoja.addMergedRegion(new CellRangeAddress(indiceFila - 1, indiceFila - 1, 0, ENCABEZADOS_PRINCIPALES.length - 1));

        CellStyle estiloTimestampIzquierda = hoja.getWorkbook().createCellStyle();
        estiloTimestampIzquierda.cloneStyleFrom(estilos.estiloTimestamp);
        estiloTimestampIzquierda.setAlignment(HorizontalAlignment.LEFT);

        crearCeldaYAplicarEstilo(hoja.createRow(indiceFila++), 0, "Generado el: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), estiloTimestampIzquierda);
        hoja.addMergedRegion(new CellRangeAddress(indiceFila - 1, indiceFila - 1, 0, ENCABEZADOS_PRINCIPALES.length - 1));

        return indiceFila + 1;
    }

    private int crearBloqueDeAsistencia(Sheet hoja, int indiceFila, EmpleadoReporte empleadoReporte, Asistencia asistencia, ExcelEstiloFactory estilos) {
        int filaInicioAsistencia = indiceFila;
        Row filaAsistencia = hoja.createRow(indiceFila++);

        // Determinar si el empleado no cumplió con las 8 horas
        TiempoTrabajo tiempoNeto = asistencia.getHorasNetasTrabajadas();
        boolean noCumplio8Horas = tiempoNeto.getHoras() < 8;
        CellStyle estiloFila = noCumplio8Horas ? estilos.estiloFilaRoja : estilos.estiloFilaPrincipal;

        // Datos básicos del empleado y asistencia
        crearCeldaYAplicarEstilo(filaAsistencia, 0, Optional.ofNullable(asistencia.getFecha())
                .map(FORMATO_FECHA::format)
                .orElse(""), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 1, empleadoReporte.getEmpleado().getCodigo(), estiloFila);

        String nombreCompleto = String.format("%s %s %s", empleadoReporte.getEmpleado()
                .getNombre(), empleadoReporte.getEmpleado()
                .getApellidoPaterno(), Optional.ofNullable(empleadoReporte.getEmpleado().getApellidoMaterno())
                .orElse("")).trim();
        crearCeldaYAplicarEstilo(filaAsistencia, 2, nombreCompleto, estiloFila);

        crearCeldaYAplicarEstilo(filaAsistencia, 3, empleadoReporte.getPuesto() != null ? empleadoReporte.getPuesto()
                .getNombre() : "", estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 4, empleadoReporte.getUnidad() != null ? empleadoReporte.getUnidad()
                .getNombre() : "", estiloFila);

        // Horarios de jornada
        crearCeldaYAplicarEstilo(filaAsistencia, 5, Optional.ofNullable(asistencia.getInicioJornada())
                .map(d -> d.toLocalDate().toString())
                .orElse(""), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 6, Optional.ofNullable(asistencia.getInicioJornada())
                .map(FORMATO_HORA_AM_PM::format)
                .orElse(""), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 7, Optional.ofNullable(asistencia.getFinJornada())
                .map(d -> d.toLocalDate().toString())
                .orElse(""), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 8, Optional.ofNullable(asistencia.getFinJornada())
                .map(FORMATO_HORA_AM_PM::format)
                .orElse(""), estiloFila);

        // Usar métodos del dominio - Solo formateo, cálculos en el modelo
        llenarTiemposDesdeModelo(filaAsistencia, asistencia, estilos);

        aplicarBordeExterno(hoja, filaInicioAsistencia, filaInicioAsistencia, 0, ENCABEZADOS_PRINCIPALES.length - 1);

        // Crear sub-tabla de pausas si existen
        if (asistencia.getPausas() != null && !asistencia.getPausas().isEmpty()) {
            int filaInicioSubTabla = indiceFila;

            Row filaSubEncabezado = hoja.createRow(indiceFila++);
            for (int i = 0; i < ENCABEZADOS_PAUSAS.length; i++) {
                crearCeldaYAplicarEstilo(filaSubEncabezado, i + INDENTACION_PAUSAS, ENCABEZADOS_PAUSAS[i], estilos.estiloSubEncabezado);
            }

            for (PausaAsistencia pausa : asistencia.getPausas()) {
                Row filaPausa = hoja.createRow(indiceFila++);
                crearCeldaYAplicarEstilo(filaPausa, INDENTACION_PAUSAS, Optional.ofNullable(pausa.getInicio())
                        .map(d -> d.toLocalDate().format(FORMATO_FECHA))
                        .orElse(""), estilos.estiloCeldaDatos);
                crearCeldaYAplicarEstilo(filaPausa, INDENTACION_PAUSAS + 1, pausa.getTipoPausa() != null ? pausa.getTipoPausa()
                        .toString() : "", estilos.estiloCeldaDatos);
                crearCeldaYAplicarEstilo(filaPausa, INDENTACION_PAUSAS + 2, Optional.ofNullable(pausa.getInicio())
                        .map(FORMATO_HORA_AM_PM::format)
                        .orElse(""), estilos.estiloCeldaDatos);
                crearCeldaYAplicarEstilo(filaPausa, INDENTACION_PAUSAS + 3, Optional.ofNullable(pausa.getFin())
                        .map(FORMATO_HORA_AM_PM::format)
                        .orElse(""), estilos.estiloCeldaDatos);

                // Usar método del dominio para obtener duración formateada
                crearCeldaYAplicarEstilo(filaPausa, INDENTACION_PAUSAS + 4, pausa.formatear(), estilos.estiloCeldaDatos);
            }

            int filaFinSubTabla = indiceFila - 1;
            aplicarBordeExterno(hoja, filaInicioSubTabla, filaFinSubTabla, INDENTACION_PAUSAS, INDENTACION_PAUSAS + ENCABEZADOS_PAUSAS.length - 1);
        }

        return indiceFila;
    }

    private void llenarTiemposDesdeModelo(Row filaAsistencia, Asistencia asistencia, ExcelEstiloFactory estilos) {
        // Usar métodos del dominio - Solo formateo, cálculos en el modelo
        TiempoTrabajo tiempoBruto = asistencia.getHorasBrutasTrabajadas();
        TiempoTrabajo tiempoNeto = asistencia.getHorasNetasTrabajadas();
        TiempoTrabajo tiempoPausaComida = asistencia.getTotalPausaComida();
        TiempoTrabajo tiempoOtrasPausas = asistencia.getTotalOtrasPausas();
        TiempoTrabajo tiempoTotalPausas = asistencia.getTotalPausas();
        TiempoTrabajo tiempoExtras = asistencia.getHorasExtras();
        int tiempoExtraNeto = asistencia.getHorasExtrasNetas();

        // Determinar si el empleado no cumplió con las 8 horas
        boolean noCumplio8Horas = tiempoNeto.getHoras() < 8;
        CellStyle estiloFila = noCumplio8Horas ? estilos.estiloFilaRoja : estilos.estiloFilaPrincipal;
        CellStyle estiloFilaEntero = noCumplio8Horas ? estilos.estiloFilaRojaEntero : estilos.estiloFilaPrincipalEntero;

        // Llenar las celdas usando solo formateo
        crearCeldaYAplicarEstilo(filaAsistencia, 9, formatearTiempoHorasMinutos(tiempoBruto), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 10, tiempoNeto.getHoras(), estiloFilaEntero);
        crearCeldaYAplicarEstilo(filaAsistencia, 11, formatearTiempoHorasMinutos(tiempoPausaComida), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 12, formatearTiempoHorasMinutos(tiempoOtrasPausas), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 13, formatearTiempoHorasMinutos(tiempoTotalPausas), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 14, formatearTiempoHorasMinutos(tiempoExtras), estiloFila);
        crearCeldaYAplicarEstilo(filaAsistencia, 15, tiempoExtras.getHoras(), estiloFilaEntero);

        // Diferencia usando método del dominio
        crearCeldaYAplicarEstilo(filaAsistencia, 16, asistencia.getDiferencia8HorasTrabajadasFormateada(), estiloFila);
    }

    private void crearCeldaYAplicarEstilo(Row fila, int columna, Object valor, CellStyle estilo) {
        Cell celda = fila.createCell(columna);
        if (valor instanceof String) {
            celda.setCellValue((String) valor);
        } else if (valor instanceof Double) {
            celda.setCellValue((Double) valor);
        } else if (valor instanceof Integer) {
            celda.setCellValue((Integer) valor);
        } else if (valor != null) {
            celda.setCellValue(valor.toString());
        }
        celda.setCellStyle(estilo);
    }

    private void aplicarBordeExterno(Sheet hoja, int filaInicio, int filaFin, int columnaInicio, int columnaFin) {
        // Implementación básica de bordes - se puede expandir según necesidades
        for (int fila = filaInicio; fila <= filaFin; fila++) {
            Row filaActual = hoja.getRow(fila);
            if (filaActual != null) {
                for (int col = columnaInicio; col <= columnaFin; col++) {
                    Cell celda = filaActual.getCell(col);
                    if (celda != null) {
                        CellStyle estiloConBorde = hoja.getWorkbook().createCellStyle();
                        estiloConBorde.cloneStyleFrom(celda.getCellStyle());
                        estiloConBorde.setBorderTop(BorderStyle.THIN);
                        estiloConBorde.setBorderBottom(BorderStyle.THIN);
                        estiloConBorde.setBorderLeft(BorderStyle.THIN);
                        estiloConBorde.setBorderRight(BorderStyle.THIN);
                        celda.setCellStyle(estiloConBorde);
                    }
                }
            }
        }
    }

    /**
     * Crea la hoja de resumen por empleado usando solo métodos del dominio.
     * Solo muestra sumatorias de horas netas y extras redondeadas.
     */
    private void crearHojaResumen(Workbook libroTrabajo, List<EmpleadoReporte> dataSource, EmpleadoReporteCommand command, ExcelEstiloFactory estilos) {
        Sheet hojaResumen = libroTrabajo.createSheet("Resumen por Empleado");

        int indiceFila = 0;

        // Crear encabezado
        indiceFila = crearEncabezadoResumen(hojaResumen, indiceFila, command, estilos);

        // Crear encabezados de columnas
        Row filaEncabezado = hojaResumen.createRow(indiceFila++);
        for (int i = 0; i < ENCABEZADOS_RESUMEN.length; i++) {
            crearCeldaYAplicarEstilo(filaEncabezado, i, ENCABEZADOS_RESUMEN[i], estilos.estiloEncabezado);
        }

        // Crear filas de datos por empleado
        for (EmpleadoReporte empleadoReporte : dataSource) {
            indiceFila = crearFilaResumenEmpleado(hojaResumen, indiceFila, empleadoReporte, estilos);
        }

        // Autoajustar columnas
        for (int i = 0; i < ENCABEZADOS_RESUMEN.length; i++) {
            hojaResumen.autoSizeColumn(i);
        }
    }

    private int crearEncabezadoResumen(Sheet hoja, int indiceFila, EmpleadoReporteCommand command, ExcelEstiloFactory estilos) {
        Row filaTitulo = hoja.createRow(indiceFila++);
        crearCeldaYAplicarEstilo(filaTitulo, 0, "RESUMEN DE HORAS POR EMPLEADO", estilos.estiloTitulo);
        hoja.addMergedRegion(new CellRangeAddress(indiceFila - 1, indiceFila - 1, 0, ENCABEZADOS_RESUMEN.length - 1));

        CellStyle estiloTimestampIzquierda = hoja.getWorkbook().createCellStyle();
        estiloTimestampIzquierda.cloneStyleFrom(estilos.estiloTimestamp);
        estiloTimestampIzquierda.setAlignment(HorizontalAlignment.LEFT);

        crearCeldaYAplicarEstilo(hoja.createRow(indiceFila++), 0, "Generado el: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), estiloTimestampIzquierda);
        hoja.addMergedRegion(new CellRangeAddress(indiceFila - 1, indiceFila - 1, 0, ENCABEZADOS_RESUMEN.length - 1));

        return indiceFila + 1;
    }

    private int crearFilaResumenEmpleado(Sheet hoja, int indiceFila, EmpleadoReporte empleadoReporte, ExcelEstiloFactory estilos) {
        Row filaEmpleado = hoja.createRow(indiceFila++);

        // Usar métodos del dominio para obtener sumatorias

        String nombreCompleto = String.format("%s %s %s", empleadoReporte.getEmpleado()
                .getNombre(), empleadoReporte.getEmpleado()
                .getApellidoPaterno(), Optional.ofNullable(empleadoReporte.getEmpleado().getApellidoMaterno())
                .orElse("")).trim();

        crearCeldaYAplicarEstilo(filaEmpleado, 0, empleadoReporte.getEmpleado()
                .getCodigo(), estilos.estiloFilaPrincipal);
        crearCeldaYAplicarEstilo(filaEmpleado, 1, nombreCompleto, estilos.estiloFilaPrincipal);
        crearCeldaYAplicarEstilo(filaEmpleado, 2, empleadoReporte.getPuesto() != null ? empleadoReporte.getPuesto()
                .getNombre() : "", estilos.estiloFilaPrincipal);
        crearCeldaYAplicarEstilo(filaEmpleado, 3, empleadoReporte.getUnidad() != null ? empleadoReporte.getUnidad()
                .getNombre() : "", estilos.estiloFilaPrincipal);
        crearCeldaYAplicarEstilo(filaEmpleado, 4, empleadoReporte.getSumatoriaTiempoTrabajado(), estilos.estiloFilaPrincipalEntero);
        crearCeldaYAplicarEstilo(filaEmpleado, 5, empleadoReporte.getSumatoriaTiempoExtras(), estilos.estiloFilaPrincipalEntero);

        return indiceFila;
    }
}
