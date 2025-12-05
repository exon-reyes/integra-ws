package integra.unidad.service;

import integra.globalexception.DataNotFoundException;
import integra.model.*;
import integra.operatividad.repository.UnidadHorarioJpaRepository;
import integra.unidad.query.UnidadContactoQuery;
import integra.unidad.query.UnidadHorarioQuery;
import integra.unidad.query.UnidadInfo;
import integra.unidad.query.UnidadInfoActivo;
import integra.unidad.repository.UnidadRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadQueryService {
    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final UnidadRepository readRepository;
    private final UnidadHorarioJpaRepository horarioRepository;

    @Cacheable(value = "unidadList", unless = "#result == null")
    @Transactional(readOnly = true)
    public List<Unidad> obtenerInfoBasica() {
        return readRepository.findBy(UnidadInfo.class).stream().map(data -> {
            Contacto contacto = new Contacto(new Zona(data.zonaNombre()));
            Empleado supervisor = new Empleado(data.supervisorNombreCompleto());
            return new Unidad(data.id(), data.clave(), data.nombre(), data.activo(), contacto, supervisor);
        }).toList();
    }

    @Cacheable(value = "unidadList", key = "#id")
    @Transactional(readOnly = true)
    public List<Unidad> obtenerInfoBasicaPorSupervisor(Integer id) {
        return readRepository.findBySupervisor_IdOrderByClaveAsc(id, UnidadInfo.class).stream().map(data -> {
            Contacto contacto = new Contacto(new Zona(data.zonaNombre()));
            return new Unidad(data.id(), data.clave(), data.nombre(), data.activo(), contacto);
        }).toList();
    }

    @Cacheable(value = "unidadesActivas")
    @Transactional(readOnly = true)
    public List<Unidad> obtenerUnidadesActivas() {
        return readRepository.findBy(UnidadInfoActivo.class)
                .stream()
                .map(data -> new Unidad(data.id(), data.nombreCompleto()))
                .toList();
    }

    @Cacheable(value = "unidadContacto", key = "#idUnidad")
    @Transactional(readOnly = true)
    public Unidad obtenerContacto(Integer idUnidad) {
        UnidadContactoQuery data = readRepository.findById(idUnidad, UnidadContactoQuery.class)
                .orElseThrow(() -> new DataNotFoundException("Sin registro", "Unidad no encontrada"));
        Contacto contacto = new Contacto(data.telefono(), data.email(), new Estado(data.estadoId(), data.estadoNombre()), data.localizacion(), new Zona(data.zonaId(), data.zonaNombre()));
        return new Unidad(data.id(), data.clave(), data.nombre(), data.activo(), contacto);
    }

    @Cacheable(value = "unidadInfo", key = "#idUnidad")
    @Transactional(readOnly = true)
    public List<HorarioOperativo> obtenerHorario(Integer idUnidad) {
        return horarioRepository.findByUnidadId(idUnidad, UnidadHorarioQuery.class)
                .stream()
                .map(x -> new HorarioOperativo(x.id(), x.operatividadNombre(), x.apertura(), x.cierre()))
                .toList();
    }

    @Transactional(readOnly = true)
    public byte[] exportarUnidadesExcel() {
        List<UnidadContactoQuery> unidades = readRepository.findBy(UnidadContactoQuery.class);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Unidades");

            // Estilos optimizados (se crean una sola vez)
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle subtitleStyle = createSubtitleStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle columnHeaderStyle = createColumnHeaderStyle(workbook);

            int currentRow = 0;

            // Encabezado principal
            currentRow = createDocumentHeader(sheet, titleStyle, subtitleStyle, currentRow);

            // Encabezados de columna
            currentRow = createColumnHeaders(sheet, columnHeaderStyle, currentRow);

            // Datos
            currentRow = fillDataRows(sheet, unidades, dataStyle, currentRow);

            // Ajustar anchos de columna de forma optimizada
            autoSizeColumns(sheet, 9);

            // Convertir a bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }

    private int createDocumentHeader(XSSFSheet sheet, CellStyle titleStyle, CellStyle subtitleStyle, int rowNum) {
        // Título principal
        Row titleRow = sheet.createRow(rowNum++);
        titleRow.setHeight((short) 600);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE | LISTA DE UNIDADES");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 8));

        // Fecha de generación
        Row dateRow = sheet.createRow(rowNum++);
        Cell dateCell = dateRow.createCell(0);
        String fechaGeneracion = LocalDateTime.now().format(FECHA_FORMATO);
        dateCell.setCellValue("Generado el " + fechaGeneracion);
        dateCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 8));

        // Versión
        Row versionRow = sheet.createRow(rowNum++);
        Cell versionCell = versionRow.createCell(0);
        versionCell.setCellValue("Versión del doc. 1.0");
        versionCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 8));


        return rowNum;
    }

    private int createColumnHeaders(XSSFSheet sheet, CellStyle columnHeaderStyle, int rowNum) {
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeight((short) 450);

        String[] headers = {"CLAVE", "NOMBRE", "EMAIL", "TELEFONO", "DIRECCIÓN",
                "COORDENADAS", "ZONA", "ESTADO", "SUPERVISOR ASIGNADO"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(columnHeaderStyle);
        }

        return rowNum;
    }

    private int fillDataRows(XSSFSheet sheet, List<UnidadContactoQuery> unidades,
                             CellStyle dataStyle, int rowNum) {
        for (UnidadContactoQuery unidad : unidades) {
            Row row = sheet.createRow(rowNum++);

            createStyledCell(row, 0, unidad.clave(), dataStyle, CellType.STRING);
            createStyledCell(row, 1, unidad.nombreCompleto(), dataStyle, CellType.STRING);
            createStyledCell(row, 2, unidad.email(), dataStyle, CellType.STRING);
            createStyledCell(row, 3, unidad.telefono(), dataStyle, CellType.STRING);
            createStyledCell(row, 4, unidad.direccion(), dataStyle, CellType.STRING);
            createStyledCell(row, 5, unidad.localizacion(), dataStyle, CellType.STRING);
            createStyledCell(row, 6, unidad.zonaNombre(), dataStyle, CellType.STRING);
            createStyledCell(row, 7, unidad.estadoNombre(), dataStyle, CellType.STRING);
            createStyledCell(row, 8, unidad.supervisorNombreCompleto(), dataStyle, CellType.STRING);
        }

        return rowNum;
    }

    private void createStyledCell(Row row, int column, String value, CellStyle style, CellType type) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
        cell.setCellType(type);
        cell.setCellStyle(style);
    }

    private CellStyle createTitleStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.ORANGE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createSubtitleStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createColumnHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        // Fondo azul
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Alineación
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        // Fondo blanco
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Bordes sutiles
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);

        return style;
    }

    private void autoSizeColumns(XSSFSheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            // Agregar un poco de padding extra
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 500);
        }
    }
}