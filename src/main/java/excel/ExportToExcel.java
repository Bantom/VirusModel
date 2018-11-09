package excel;

import model.StatisticsAndCoefficients;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExportToExcel {
    private static String[] columns = {
            "quantityOfPeople",
            "minPeoples",
            "maxPeoples",
            "minContactsBecameIll",
            "maxContactsBecameIll",
            "probability",
            "complicationProbabilityY",
            "complicationProbabilityO",
            "susceptibleProbability"
    };

    public static void writeToExcel(StatisticsAndCoefficients coefficients, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Coefficients");
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Fill cells with data
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(coefficients.getCoefficients().getQuantityOfPeople());
        row.createCell(1).setCellValue(coefficients.getCoefficients().getMinPeoples());
        row.createCell(2).setCellValue(coefficients.getCoefficients().getMaxPeoples());
        row.createCell(3).setCellValue(coefficients.getCoefficients().getMinContactsBecameIll());
        row.createCell(4).setCellValue(coefficients.getCoefficients().getMaxContactsBecameIll());
        row.createCell(5).setCellValue(coefficients.getCoefficients().getProbability());
        row.createCell(6).setCellValue(coefficients.getCoefficients().getComplicationProbabilityY());
        row.createCell(7).setCellValue(coefficients.getCoefficients().getComplicationProbabilityO());
        row.createCell(8).setCellValue(coefficients.getCoefficients().getSusceptibleProbability());

        // Resize all columns to fit the content size
        for(int j = 0; j < columns.length; j++) {
            sheet.autoSizeColumn(j);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(fileName + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
