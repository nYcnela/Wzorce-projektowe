package ma.swiftrent.pattern.bridge.storage;

import ma.swiftrent.service.storage.FileStorageService;
import org.springframework.web.multipart.MultipartFile;
/*
Tydzień 9, Jasne i zrozumiałe nazwy 2
Nazwa "JsonStorageImplementor" daj nam do zrozumienia, że ta klasa implementuje
przechowywanie plików typu Json
"jsonAdapter" nie pozostawia złudzenia co do funkcji tego pola,
podobnie jak nazwa metody "storeFile"
 */
public class JsonStorageImplementor implements StorageImplementor {

    private final FileStorageService jsonAdapter;

    public JsonStorageImplementor(FileStorageService jsonAdapter) {
        this.jsonAdapter = jsonAdapter;
    }

    @Override
    public String storeFile(MultipartFile file) {
        return jsonAdapter.store(file);
    }
}
//Koniec, Tydzień 9, Jasne i zrozumiałe nazwy 2