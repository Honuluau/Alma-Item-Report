package honuluau.almaitemreport;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AlmaItemReport {

    private static final Logger log = LogManager.getFormatterLogger(AlmaItemReport.class);

    public static void readXLSX(File file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow headerRow = sheet.getRow(0);

            for (Cell cell : headerRow) {
                log.info(cell.toString());
            }
            workbook.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileExtension(String filePath) {
        try{
            String fileExtenssion = filePath.substring(filePath.lastIndexOf("."));
            return fileExtenssion;
        } catch(Exception e) {
            return "";
        }
    }

    public static String getFilePathFromUser() {
        System.out.println("Please enter a .xlsx file path:\n");

        Scanner scanner = new Scanner(System.in);
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
                readXLSX(new File(filePath));
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
    }
}
