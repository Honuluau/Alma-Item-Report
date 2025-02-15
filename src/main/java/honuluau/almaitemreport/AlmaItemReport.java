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

    private static List<String> columnNames = new ArrayList<String>();
    private static List<XSSFRow> rows = new ArrayList<XSSFRow>();

    private static XSSFSheet sheet;

    // Reads XLSX File.
    public static void readXLSX(File file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            sheet = workbook.getSheetAt(0);
            XSSFRow headerRow = sheet.getRow(0);

            // Add ColumnNames
            for (Cell cell : headerRow) {
                columnNames.add(cell.toString());
                log.info(cell.toString());
            }

            // Add rows, except for the header row of course.
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                rows.add(sheet.getRow(i));
            }

            log.info(sheet.getPhysicalNumberOfRows()-1 + " item(s) have been found.");
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
            System.out.println("File not found or not a .xlsx file.");
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

    public static void printInCollumns(List<String> stringList) {
        // Print stringList into 5 columns.
        int printColumns = 5;

        List<List<String>> printRows = new ArrayList<>();

        // For loop runs based on multiples of 5.
        for (int i = 0; i < stringList.size(); i += printColumns) {
            // Add row to printRows
            List<String> printRow = new ArrayList<>();
            for (int r = 0; r < printColumns && i + r < stringList.size(); r++) {
                printRow.add(stringList.get(i + r));
            }
            printRows.add(printRow);
        }

        int largestWidth = 0;
        int widthMargin = 4;
        for (String name : stringList) {
            if (name.length() > largestWidth) {
                largestWidth = name.length();
            }
        }        

        // Print Rows
        for (List<String> printRow : printRows) {
            for (String print : printRow) {
                System.out.printf("%-"+(largestWidth+widthMargin)+"s", print);
            }
            System.out.println();
        }
    }

    public static void promptCommand() {
        System.out.println("Please enter a pull command:");
        System.out.print("> ");

        String pullCommand = scanner.nextLine();
        interpretPullCommand(pullCommand);
    }

    // Turns underscores into spaces and removes quotation marks from input.
    public static String formatString(String s) {
        String formattedString = s;

        formattedString = formattedString.replaceAll("_", " ");
        formattedString = formattedString.replaceAll("\"", "");

        return formattedString;
    }

    public static void interpretPullCommand(String command) {
        String[] splitCommands = command.split(" ");
        System.out.println("---------------------");

        if (!splitCommands[0].equals("PULL") || !splitCommands[2].equals("WHERE")) {
            log.error("PULL and WHERE not indexed properly, make sure they are in the right place.");
            promptCommand();
        }

        try {
            String[] pullColumnNames = splitCommands[1].split(",");
            String whereColumnNames = splitCommands[3];

            String[] whereCommands = whereColumnNames.split("=");
            String columnComparator = whereCommands[0];
            String[] columnEquals = whereCommands[1].split(",");

            for (String pullColumnName : pullColumnNames) {
                String formattedColumnName = formatString(pullColumnName);
                log.info("Pulling " + formattedColumnName);

                for (String columnValue : columnEquals) {
                    String formattedValue = formatString(columnValue);
                    log.info("With " + formattedValue);

                    for (XSSFRow row : rows) {
                        log.info("Parsing row " + row.getRowNum());
                    }
                }
            }
        } catch(Exception e) {
            log.error(e);
            log.error("Invalid command input.");
        }
    }

    public static void main(String[] args) {
        // Run Alma Item Report
        System.out.println("Alma Item Report Running! \n----------------------------------------\n");

        String filePath = getFilePathFromUser();
        readXLSX(new File(filePath));

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("Available Columns/Keys:\n");

        printInCollumns(columnNames);

        System.out.println("\nCommand template: PULL \"columnA\",\"columnB\" WHERE \"columnC\"=\"value1\",\"value2\" \n*** Please use underscores instead of spaces in variables."); 
        System.out.println("--------------------------------------------------------------------------------");
        promptCommand();

        scanner.close();
    }
}
