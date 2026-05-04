package ma.swiftrent.pattern.bridge.storage;

import org.springframework.web.multipart.MultipartFile;

public class ImageStorage extends FileStorage {

    public ImageStorage(StorageImplementor implementor) {
        super(implementor);
    }

    @Override
    public String store(MultipartFile file) {
        System.out.println("📷 Zapisywanie obrazu...");
        return implementor.storeFile(file);
    }
}
