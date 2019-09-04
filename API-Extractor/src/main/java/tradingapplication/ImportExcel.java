package tradingapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.poi.ss.usermodel.*;

public class ImportExcel {

    public static ArrayList<String> importSymbolsFromExcel(String path) {
        //importing excel file with id symbols values as list
        ArrayList<String> allSymbols = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(new File(path));
            //getting the sheet at index zero
            Sheet sheet = workbook.getSheetAt(0);
            //create a dataformatter to format and get each cell'log value as string
            DataFormatter dataFormatter = new DataFormatter();
            //for-each loop to iterate over the rows and columns
            for (Row row : sheet) {
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell).toLowerCase();
                    if (!cellValue.equals("symbol")) {
                        allSymbols.add(cellValue);
                    }
                }
            }
        } catch (IOException e) {
            CustomLogger log = new CustomLogger();
            log.addToLog("Entry failed" + "\n");
            log.addToLog(System.getProperty("line.separator"));
        }
        return allSymbols;
    }
}
