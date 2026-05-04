package ma.swiftrent.pattern.singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Tydzień 2, Wzorzec Singleton 1 (Static Block Initialization)
// Konfiguracja uploadów powinna być wspólna dla całej aplikacji.
// Singleton centralizuje katalog zapisu plików i sposób budowania publicznego URL,
// dzięki czemu CarService nie trzyma już własnej, lokalnej konfiguracji.
public final class UploadStorageSettings {

    private static final String DEFAULT_UPLOAD_DIRECTORY = "uploads";
    private static final String DEFAULT_PUBLIC_BASE_URL = "http://localhost:8080/uploads/";
    private static final UploadStorageSettings INSTANCE;

    /*
     Tydzień 9, Zwracanie wyjątku zamiast kodu błędu
     W tym przypadku wyjątek jest zwracany w strukturze try-catch
     */

    static {
        try {
            INSTANCE = new UploadStorageSettings(DEFAULT_UPLOAD_DIRECTORY, DEFAULT_PUBLIC_BASE_URL);
        } catch (IOException exception) {
            throw new IllegalStateException("Nie można zainicjalizować konfiguracji uploadów.", exception);
        }
    }
    //Koniec, Tydzień 9, Zwracanie wyjątku zamiast kodu błędu

    private final Path storageDirectory;
    private final String publicBaseUrl;

    private UploadStorageSettings(String uploadDirectory, String publicBaseUrl) throws IOException {
        this.storageDirectory = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        Files.createDirectories(this.storageDirectory);
        this.publicBaseUrl = publicBaseUrl;
    }

    public static UploadStorageSettings getInstance() {
        return INSTANCE;
    }

    public Path resolveTargetLocation(String fileName) {
        return storageDirectory.resolve(fileName);
    }

    public String buildPublicUrl(String fileName) {
        return publicBaseUrl + fileName;
    }

    public Path getStorageDirectory() {
        return storageDirectory;
    }
}
// Koniec, Tydzień 2, Wzorzec Singleton 1
