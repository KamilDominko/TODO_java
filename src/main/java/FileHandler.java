import java.io.File;
import java.io.IOException;

public class FileHandler {
    private static final String FILE_PATH = "tasks.json";

    public static void checkAndCreateFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Błąd podczas tworzenia pliku: " + e.getMessage());
            }
        }
    }
}
