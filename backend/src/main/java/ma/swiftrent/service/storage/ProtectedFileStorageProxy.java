package ma.swiftrent.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Set;

// Tydzień 4, Wzorzec Proxy 1
// Zapis plików jest chroniony przez pośrednika, który sprawdza bezpieczeństwo i podstawową poprawność danych.
// Proxy kontroluje dostęp do rzeczywistego storage, dzięki czemu CarService nie musi znać szczegółów walidacji plików.
@Service
@Primary
@RequiredArgsConstructor
public class ProtectedFileStorageProxy implements FileStorageService {

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    @Qualifier("localFileStorageServiceTarget")
    private final FileStorageService fileStorageService;

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new RuntimeException("Plik musi miec poprawna nazwe");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new RuntimeException("Plik jest za duzy. Maksymalny rozmiar to 5 MB");
        }

        String extension = extractExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new RuntimeException("Dozwolone sa tylko pliki JPG, JPEG, PNG oraz WEBP");
        }

        return fileStorageService.store(file);
    }

    private String extractExtension(String fileName) {
        int extensionSeparator = fileName.lastIndexOf('.');
        if (extensionSeparator < 0 || extensionSeparator == fileName.length() - 1) {
            throw new RuntimeException("Plik musi miec rozszerzenie");
        }

        return fileName.substring(extensionSeparator + 1).toLowerCase(Locale.ROOT);
    }
}
// Koniec, Tydzień 4, Wzorzec Proxy 1
