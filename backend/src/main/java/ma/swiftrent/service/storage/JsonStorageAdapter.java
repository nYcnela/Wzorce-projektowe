package ma.swiftrent.service.storage;

import org.springframework.web.multipart.MultipartFile;

/*
    Tydzień 2, Wzorzec Adapter 3
    Adapter pomiędzy interfejsem do zapisu plików, a klasą do zapisu plików json
 */
public class JsonStorageAdapter implements FileStorageService {

    private final JsonStorageSystem jsonStorageSystem;

    public JsonStorageAdapter(JsonStorageSystem jsonStorageSystem) {
        this.jsonStorageSystem = jsonStorageSystem;
    }

    @Override
    public String store(MultipartFile file) {

        try {
            byte[] data = file.getBytes();
            String fileName = file.getOriginalFilename();

            return jsonStorageSystem.saveAsJson(data, fileName);

        } catch (Exception e) {
            throw new RuntimeException("Błąd zapisu JSON", e);
        }

    }
}
//Koniec, Tydzień 2, Wzorzec Adapter 3
