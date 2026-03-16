package ma.swiftrent.service.storage;

public class JsonStorageSystem {

    public String saveAsJson(byte[] data, String fileName) {

        System.out.println("Saving file as JSON: " + fileName);

        return "/json-storage/" + fileName + ".json";
    }

}
