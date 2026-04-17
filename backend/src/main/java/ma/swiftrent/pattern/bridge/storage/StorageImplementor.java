package ma.swiftrent.pattern.bridge.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageImplementor {
    String store(MultipartFile file);
}
