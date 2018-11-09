package excel;

import model.Coefficients;
import model.StatisticsGVI;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportFromExcel {

    public static List<StatisticsGVI> readSeasonsFromFile(String fileName) {
        List<StatisticsGVI> result = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(new File(fileName)));
            Sheet datatypeSheet = workbook.getSheetAt(0);

            for (Row currentRow : datatypeSheet) {
                Iterator<Cell> cellIterator = currentRow.iterator();
                StatisticsGVI statictics = new StatisticsGVI();
                int i = 1;
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    if (i == 1) {
                        statictics.setI(currentCell.getNumericCellValue());
                    }
                    if (i == 2) {
                        statictics.setG(currentCell.getNumericCellValue());
                    }
                    if (i == 3) {
                        statictics.setV(currentCell.getNumericCellValue());
                    }
                    i++;
                }
                result.add(statictics);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Coefficients readCoefficientsFromFile(String fileName) {
        Coefficients coefficients = new Coefficients();
        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(new File(fileName)));
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Row currentRow = datatypeSheet.getRow(1);
            Iterator<Cell> cellIterator = currentRow.iterator();
            int i = 1;
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                if (i == 1) {
                    coefficients.setQuantityOfPeople((int) currentCell.getNumericCellValue());
                }
                if (i == 2) {
                    coefficients.setMinPeoples((int) currentCell.getNumericCellValue());
                }
                if (i == 3) {
                    coefficients.setMaxPeoples((int) currentCell.getNumericCellValue());
                }
                if (i == 4) {
                    coefficients.setMinContactsBecameIll((int) currentCell.getNumericCellValue());
                }
                if (i == 5) {
                    coefficients.setMaxContactsBecameIll((int) currentCell.getNumericCellValue());
                }
                if (i == 6) {
                    coefficients.setProbability(currentCell.getNumericCellValue());
                }
                if (i == 7) {
                    coefficients.setComplicationProbabilityY(currentCell.getNumericCellValue());
                }
                if (i == 8) {
                    coefficients.setComplicationProbabilityO(currentCell.getNumericCellValue());
                }
                if (i == 9) {
                    coefficients.setSusceptibleProbability(currentCell.getNumericCellValue());
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coefficients;
    }
}
