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

    public static void addPrototype(String key, User user) {
        prototypes.put(key, user);
    }

    public static User getPrototype(String key) {
        User prototype = prototypes.get(key);
        return prototype != null ? prototype.clone() : null;
    }
}
//Koniec, Tydzień 2, Wzorzec Prototype 2
