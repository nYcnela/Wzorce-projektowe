package ma.swiftrent.pattern.bridge.storage;

import ma.swiftrent.service.storage.FileStorageService;
import org.springframework.web.multipart.MultipartFile;

public class LocalStorageImplementor implements StorageImplementor {

    private final FileStorageService localStorage;

    public LocalStorageImplementor(FileStorageService localStorage) {
        this.localStorage = localStorage;
    }

    @Override
    public String store(MultipartFile file) {
        return localStorage.store(file);
    }
}
