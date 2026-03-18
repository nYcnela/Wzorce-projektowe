package ma.swiftrent.pattern.observer.favorite;

// Event przenosi dane o dodaniu lub usunieciu auta
// z listy ulubionych uzytkownika.
public record FavoriteChangedEvent(
        String userEmail,
        Long carId,
        boolean added
) {
}
