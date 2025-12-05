package integra.asistencia.service.compensacion;

import integra.asistencia.actions.EmpleadoReporteCommand;
import integra.asistencia.entity.CompensacionSalidaDepositoEntity;
import integra.asistencia.query.CompensacionReporteQuery;
import integra.asistencia.specification.CompensacionSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompensacionQueryService {

    private final EntityManager entityManager;

    public List<CompensacionReporteQuery> obtenerInforme(EmpleadoReporteCommand request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompensacionReporteQuery> query = cb.createQuery(CompensacionReporteQuery.class);
        var root = query.from(CompensacionSalidaDepositoEntity.class);

        var spec = CompensacionSpecification.findByCriteriaProjection(request);
        var predicate = spec.toPredicate(root, query, cb);
        return entityManager.createQuery(query.where(predicate)).getResultList();
    }

    public byte[] obtenerCompensacionesExcel(EmpleadoReporteCommand request) {
        List<CompensacionReporteQuery> compensaciones = obtenerInforme(request);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Compensaciones");

            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Colaborador");
            headerRow.createCell(1).setCellValue("Unidad");
            headerRow.createCell(2).setCellValue("Fecha");
            headerRow.createCell(3).setCellValue("Hora Salida");
            headerRow.createCell(4).setCellValue("Horas Trabajadas");
            headerRow.createCell(5).setCellValue("Horas Faltantes");
            headerRow.createCell(6).setCellValue("Tiempo Compensado");

            // Llenar datos
            int rowNum = 1;
            for (CompensacionReporteQuery compensacion : compensaciones) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(compensacion.colaborador());
                row.createCell(1).setCellValue(compensacion.unidad());
                row.createCell(2).setCellValue(compensacion.fecha().toString());
                row.createCell(3).setCellValue(compensacion.horaSalida().toString());
                row.createCell(4).setCellValue(compensacion.horasTrabajadas().toString());
                row.createCell(5).setCellValue(compensacion.horasFaltantes().toString());
                row.createCell(6).setCellValue(compensacion.tiempoCompensado().toString());
            }

            // Auto ajustar columnas
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }
}
