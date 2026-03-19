package ma.swiftrent.pattern.template.favorite;

import ma.swiftrent.pattern.observer.favorite.FavoriteChangedEvent;
import ma.swiftrent.pattern.observer.favorite.FavoriteChangedObserver;

// Tydzień 6, Wzorzec Template 3
// Obsługa zdarzenia zmiany ulubionych przebiega w niezmiennym schemacie:
// sprawdzenie eventu, przygotowanie komunikatu i wykonanie właściwej reakcji.
// Podklasy zmieniają tylko createMessage() oraz handleMessage().
public abstract class FavoriteChangedObserverTemplate implements FavoriteChangedObserver {

    @Override
    public final void onFavoriteChanged(FavoriteChangedEvent event) {
        validate(event);
        String message = createMessage(event);
        handleMessage(message);
    }

    protected void validate(FavoriteChangedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Zdarzenie zmiany ulubionych nie może być nullem");
        }
    }

    protected abstract String createMessage(FavoriteChangedEvent event);

    protected abstract void handleMessage(String message);
}
// Koniec, Tydzień 6, Wzorzec Template 3
