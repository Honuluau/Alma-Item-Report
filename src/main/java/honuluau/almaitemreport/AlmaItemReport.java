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

    public static String getFilePathFromUser() {
        System.out.println("Please enter a .xlsx file path:\n");

        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        filePath = filePath.replaceAll("\"", "");

        String fileExtenstion = filePath.substring(filePath.lastIndexOf("."));
        System.out.println(fileExtenstion);

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
    }
}
