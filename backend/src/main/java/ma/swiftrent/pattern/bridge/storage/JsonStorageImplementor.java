package ma.swiftrent.pattern.bridge.storage;

import ma.swiftrent.service.storage.FileStorageService;
import org.springframework.web.multipart.MultipartFile;

public class JsonStorageImplementor implements StorageImplementor {

    private final FileStorageService jsonAdapter;

    public JsonStorageImplementor(FileStorageService jsonAdapter) {
        this.jsonAdapter = jsonAdapter;
    }

    @Override
    public String store(MultipartFile file) {
        return jsonAdapter.store(file);
    }
}
