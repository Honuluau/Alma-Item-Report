package honuluau.almaitemreport;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class AlmaItemReport {

    public static void readXLSX(File file) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println("Alma Item Report Running! \n----------\nPlease enter .xlsx file path:\n");

        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        filePath = filePath.replaceAll("\"", "");
        scanner.close();

        System.out.println("You're file path=" + filePath);
    }
}
