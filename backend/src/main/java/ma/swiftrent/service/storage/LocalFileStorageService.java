package ma.swiftrent.service.storage;

import ma.swiftrent.pattern.singleton.UploadStorageSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service("localFileStorageServiceTarget")
public class LocalFileStorageService implements FileStorageService {

    private final UploadStorageSettings uploadStorageSettings = UploadStorageSettings.getInstance();

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Nazwa pliku zawiera niepoprawna sekwencje " + fileName);
            }

            String newFileName = UUID.randomUUID() + "_" + fileName;
            Path targetLocation = uploadStorageSettings.resolveTargetLocation(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uploadStorageSettings.buildPublicUrl(newFileName);
        } catch (IOException ex) {
            throw new RuntimeException("Nie mozna zapisac " + fileName + ". Sprobuj ponownie", ex);
        }
    }
}
