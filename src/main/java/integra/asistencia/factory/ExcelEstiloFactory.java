package integra.asistencia.factory;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * Clase utilitaria para crear y gestionar estilos de Excel.
 * Encapsula la creación de todos los estilos de celda reutilizables.
 */
public class ExcelEstiloFactory {

    public final CellStyle estiloEncabezado;
    public final CellStyle estiloCabeceraEmpleado;
    public final CellStyle estiloTitulo;
    public final CellStyle estiloTimestamp;
    public final CellStyle estiloCeldaDatos;
    public final CellStyle estiloEtiquetaResumen;
    public final CellStyle estiloValorResumen;
    public final CellStyle estiloEtiquetaHorasExtras;
    public final CellStyle estiloValorHorasExtras;
    public final CellStyle estiloFilaPrincipal;
    public final CellStyle estiloSubEncabezado;
    public final CellStyle estiloNumeroDecimal;
    public final CellStyle estiloNumeroEntero;
    public final CellStyle estiloFilaPrincipalDecimal;
    public final CellStyle estiloFilaPrincipalEntero;
    public final CellStyle estiloDescripcion;
    public final CellStyle estiloFilaRoja;
    public final CellStyle estiloFilaRojaEntero;

    public ExcelEstiloFactory(Workbook libroTrabajo) {
        this.estiloEncabezado = crearEstiloEncabezado(libroTrabajo);
        this.estiloCabeceraEmpleado = crearEstiloCabeceraEmpleado(libroTrabajo);
        this.estiloTitulo = crearEstiloTitulo(libroTrabajo);
        this.estiloTimestamp = crearEstiloTimestamp(libroTrabajo);
        this.estiloCeldaDatos = crearEstiloCeldaDatos(libroTrabajo);
        this.estiloEtiquetaResumen = crearEstiloEtiquetaResumen(libroTrabajo);
        this.estiloValorResumen = crearEstiloValorResumen(libroTrabajo);
        this.estiloEtiquetaHorasExtras = crearEstiloEtiquetaHorasExtras(libroTrabajo);
        this.estiloValorHorasExtras = crearEstiloValorHorasExtras(libroTrabajo);
        this.estiloFilaPrincipal = crearEstiloFilaPrincipal(libroTrabajo,
                null);
        this.estiloSubEncabezado = crearEstiloSubEncabezado(libroTrabajo);
        this.estiloNumeroDecimal = crearEstiloNumero(libroTrabajo,
                "0.0");
        this.estiloNumeroEntero = crearEstiloNumero(libroTrabajo,
                "0");
        this.estiloFilaPrincipalDecimal = crearEstiloFilaPrincipal(libroTrabajo,
                "0.0");
        this.estiloFilaPrincipalEntero = crearEstiloFilaPrincipal(libroTrabajo,
                "0");
        this.estiloDescripcion = crearEstiloDescripcion(libroTrabajo);
        this.estiloFilaRoja = crearEstiloFilaRoja(libroTrabajo, null);
        this.estiloFilaRojaEntero = crearEstiloFilaRoja(libroTrabajo, "0");
    }

    private void aplicarBordes(CellStyle estilo) {
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);
        estilo.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estilo.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estilo.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estilo.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    }

    private CellStyle crearEstiloEncabezado(Workbook libroTrabajo) {
        Font fuenteEncabezado = libroTrabajo.createFont();
        fuenteEncabezado.setBold(true);
        fuenteEncabezado.setColor(IndexedColors.WHITE.getIndex());
        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteEncabezado);
        estilo.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        aplicarBordes(estilo);
        return estilo;
    }

    private CellStyle crearEstiloCabeceraEmpleado(Workbook libroTrabajo) {
        Font fuenteEncabezado = libroTrabajo.createFont();
        fuenteEncabezado.setBold(true);
        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteEncabezado);
        estilo.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.LEFT);
        aplicarBordes(estilo);
        return estilo;
    }

    private CellStyle crearEstiloCeldaDatos(Workbook libroTrabajo) {
        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        return estilo;
    }

    private CellStyle crearEstiloEtiquetaResumen(Workbook libroTrabajo) {
        Font fuenteNegrita = libroTrabajo.createFont();
        fuenteNegrita.setBold(true);
        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteNegrita);
        estilo.setAlignment(HorizontalAlignment.RIGHT);
        aplicarBordes(estilo);
        return estilo;
    }

    private CellStyle crearEstiloValorResumen(Workbook libroTrabajo) {
        Font fuenteNegrita = libroTrabajo.createFont();
        fuenteNegrita.setBold(true);
        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteNegrita);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        aplicarBordes(estilo);
        return estilo;
    }

    private CellStyle crearEstiloEtiquetaHorasExtras(Workbook libroTrabajo) {
        Font fuenteNegrita = libroTrabajo.createFont();
        fuenteNegrita.setBold(true);

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteNegrita);
        estilo.setAlignment(HorizontalAlignment.RIGHT);
        estilo.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        aplicarBordes(estilo);

        return estilo;
    }

    private CellStyle crearEstiloValorHorasExtras(Workbook libroTrabajo) {
        Font fuenteNegrita = libroTrabajo.createFont();
        fuenteNegrita.setBold(true);

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteNegrita);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        aplicarBordes(estilo);

        return estilo;
    }

    private CellStyle crearEstiloTitulo(Workbook libroTrabajo) {
        Font fuenteTitulo = libroTrabajo.createFont();
        fuenteTitulo.setBold(true);
        fuenteTitulo.setFontHeightInPoints((short) 16);

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteTitulo);
        estilo.setAlignment(HorizontalAlignment.LEFT);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);

        return estilo;
    }

    private CellStyle crearEstiloTimestamp(Workbook libroTrabajo) {
        Font fuenteTimestamp = libroTrabajo.createFont();
        fuenteTimestamp.setItalic(true);

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteTimestamp);
        estilo.setAlignment(HorizontalAlignment.RIGHT);

        return estilo;
    }

    private CellStyle crearEstiloFilaPrincipal(Workbook libroTrabajo, String formato) {
        Font fuenteFilaPrincipal = libroTrabajo.createFont();
        fuenteFilaPrincipal.setFontHeightInPoints((short) 9);

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteFilaPrincipal);

        if (estilo instanceof XSSFCellStyle) {
            byte[] rgb = new byte[]{(byte) 226, (byte) 226, (byte) 226};
            XSSFColor customColor = new XSSFColor(rgb,
                    null);
            ((XSSFCellStyle) estilo).setFillForegroundColor(customColor);
            estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        if (formato != null) {
            DataFormat dataFormat = libroTrabajo.createDataFormat();
            estilo.setDataFormat(dataFormat.getFormat(formato));
        }
        return estilo;
    }

    private CellStyle crearEstiloSubEncabezado(Workbook libroTrabajo) {
        Font fuenteEncabezado = libroTrabajo.createFont();
        fuenteEncabezado.setBold(true);
        fuenteEncabezado.setColor(IndexedColors.BLACK.getIndex());

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteEncabezado);
        estilo.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);

        return estilo;
    }

    private CellStyle crearEstiloNumero(Workbook libroTrabajo, String formato) {
        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.cloneStyleFrom(this.estiloCeldaDatos);

        DataFormat dataFormat = libroTrabajo.createDataFormat();
        estilo.setDataFormat(dataFormat.getFormat(formato));
        return estilo;
    }

    private CellStyle crearEstiloDescripcion(Workbook libroTrabajo) {
        Font fuenteDescripcion = libroTrabajo.createFont();
        fuenteDescripcion.setFontHeightInPoints((short) 9);
        fuenteDescripcion.setItalic(true);
        fuenteDescripcion.setColor(IndexedColors.GREY_80_PERCENT.getIndex());

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteDescripcion);
        estilo.setAlignment(HorizontalAlignment.LEFT);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        estilo.setWrapText(true);
        return estilo;
    }

    private CellStyle crearEstiloFilaRoja(Workbook libroTrabajo, String formato) {
        Font fuenteFilaRoja = libroTrabajo.createFont();
        fuenteFilaRoja.setFontHeightInPoints((short) 9);

        CellStyle estilo = libroTrabajo.createCellStyle();
        estilo.setFont(fuenteFilaRoja);

        if (estilo instanceof XSSFCellStyle) {
            byte[] rgb = new byte[]{(byte) 255, (byte) 200, (byte) 200};
            XSSFColor customColor = new XSSFColor(rgb, null);
            ((XSSFCellStyle) estilo).setFillForegroundColor(customColor);
            estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        if (formato != null) {
            DataFormat dataFormat = libroTrabajo.createDataFormat();
            estilo.setDataFormat(dataFormat.getFormat(formato));
        }
        return estilo;
    }
}
