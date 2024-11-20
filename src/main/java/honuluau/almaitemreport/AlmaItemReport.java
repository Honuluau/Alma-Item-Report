package honuluau.almaitemreport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AlmaItemReport {

    private static final Logger log = LogManager.getFormatterLogger(AlmaItemReport.class);
    private static Scanner scanner = new Scanner(System.in);

    private static List<String> collumnNames = new ArrayList<String>();

    // Reads XLSX File.
    public static void readXLSX(File file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow headerRow = sheet.getRow(0);

            // Add CollumnNames
            for (Cell cell : headerRow) {
                collumnNames.add(cell.toString());
                log.info(cell.toString());
            }


            log.info(sheet.getPhysicalNumberOfRows() + " item(s) have been found.");
            workbook.close();
            
        } catch (Exception e) {
            log.error(e);
        }
    }

    // Gets the .extension from filePath
    public static String getFileExtension(String filePath) {
        try{
            String fileExtension = filePath.substring(filePath.lastIndexOf("."));
            return fileExtension;
        } catch(Exception e) {
            return "";
        }
    }

    // Takes file path from input by scanning.
    public static String getFilePathFromUser() {
        System.out.println("Please enter a .xlsx file path:\n");

        String filePath = scanner.nextLine();
        filePath = filePath.replaceAll("\"", "");
        
        // Check if its an .xlsx file
        String fileExtension = getFileExtension(filePath);
        if (!fileExtension.equals(".xlsx")) {
            System.out.println("File not found or either not an .xlsx file.");
            return getFilePathFromUser();
        }

        // Check if file path exists.
        try {
            File file = new File(filePath);
            
            if (file.exists()) {
                System.out.println("File found.");
                return filePath;
            } else {
                System.out.println("File not found.");
                return getFilePathFromUser();
            }
        } catch(Exception e) {
            System.out.println("File not found.");
            return getFilePathFromUser();
        }
    }

    public static void main(String[] args) {
        System.out.println("Alma Item Report Running! \n----------------------------------------\n");

        String filePath = getFilePathFromUser();
        readXLSX(new File(filePath));

        scanner.close();
    }
}
