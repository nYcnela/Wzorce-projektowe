package ma.swiftrent.pattern.observer.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// Tydzień 6, Wzorzec Observer 3
// Zmiana listy ulubionych w UserService tworzy zdarzenie FavoriteChangedEvent.
// Subject powiadamia observerów odpowiedzialnych za logowanie i powiadomienia,
// dzięki czemu reakcje na zmianę ulubionych pozostają odseparowane od serwisu użytkownika.
@Component
@RequiredArgsConstructor
public class FavoriteChangedSubject {

    private final List<FavoriteChangedObserver> observers;

    public void notifyObservers(FavoriteChangedEvent event) {
        observers.forEach(observer -> observer.onFavoriteChanged(event));
    }

}
// Koniec, Tydzień 6, Wzorzec Observer 3
