package ma.swiftrent.pattern.prototype;

import ma.swiftrent.entity.User;
import java.util.HashMap;
import java.util.Map;

    /*
        Tydzień 2, Wzorzec Prototype 2
        Przechowywanie prototypów użytkowników w UserPrototypeRegistry
    */
public class UserPrototypeRegistry {

    private static final Map<String, User> prototypes = new HashMap<>();

    /*
    Tydzień 9, Jedna rola funkcji 3
    Ta funkcja odpowiada tylko i wyłącznie za dodanie nowego prototypu użytkownika
     */
    public static void addPrototype(String key, User user) {
        prototypes.put(key, user);
    }
    //Koniec, Tydzień 9, Jedna rola funkcji 3

    public static User getPrototype(String key) {
        User prototype = prototypes.get(key);
        return prototype != null ? prototype.clone() : null;
    }
}
//Koniec, Tydzień 2, Wzorzec Prototype 2
