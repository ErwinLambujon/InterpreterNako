import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import Analysis.Interpreter;

public class App {
    public static void main(String[] args) {
        String codeFilePath = "D:\\BSCS-3\\BSCS3 - SECOND SEMESTER\\CS322 - PROGRAMMING LANGUAGES\\code.txt"; // Update this with the path to your text file

        try {
            // Read the code from the text file
            String code = Files.readString(Paths.get(codeFilePath)).replace("\r", "");
            System.out.println(code);

            // Execute the interpreter
            Interpreter program = new Interpreter(code);
            program.execute(null);

            boolean hasDisplay = code.contains("DISPLAY:");

            if(!hasDisplay){
                System.out.println("\nNo error");
            }

            // Check if the code contains DISPLAY tokens

            // Print "No error" only if no DISPLAY token is found
        } catch (Exception e) {
        }
    }
}
