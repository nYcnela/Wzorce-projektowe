package ma.swiftrent.pattern.observer.favorite;

// Interfejs observera definiuje wspolna reakcje
// na zmiane listy ulubionych.
public interface FavoriteChangedObserver {
    void onFavoriteChanged(FavoriteChangedEvent event);
}
