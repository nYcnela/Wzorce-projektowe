package ma.swiftrent.pattern.bridge.storage;

import org.springframework.web.multipart.MultipartFile;

/*
    Tydzień 3, Wzorzec Bridge 2
    Oddzielenie tego co zapisujemy od tego gdzie zapisujemy.
    Można w przyszłość dodać np. VideoStorage i CloudStorageImplementator
    bez potrzeby tworzenia np. ImageCloudStorageImplementator i innych kombinacji.
 */
public abstract class FileStorage {

    protected final StorageImplementor implementor;

    protected FileStorage(StorageImplementor implementor) {
        this.implementor = implementor;
    }

    public abstract String store(MultipartFile file);
}
//Koniec Tydzień 3, Wzorzec Bridge 2