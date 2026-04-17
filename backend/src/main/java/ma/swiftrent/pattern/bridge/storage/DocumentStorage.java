package ma.swiftrent.pattern.bridge.storage;

import org.springframework.web.multipart.MultipartFile;

public class DocumentStorage extends FileStorage {

    public DocumentStorage(StorageImplementor implementor) {
        super(implementor);
    }

    @Override
    public String store(MultipartFile file) {
        System.out.println("📄 Zapisywanie dokumentu...");
        return implementor.store(file);
    }
}
