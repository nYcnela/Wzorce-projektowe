package ma.swiftrent.composite.notification;

/*
    Tydzień 3, Wzorzec Composite 3
    Composite ostał wykorzystany w celu stworzenia systemu powiadomień,
    gdzie pojedyńcze drogi poiwadomień, jak i grupy składające się z tych dróg
    implementują ten sam interfejs i mogą być traktowane w taki sam sposób.
    Wywołanie send na grupie powiadomień spowoduje rekurencyjne wywołanie send
    na odpowiednich kanałach tej grupy
 */
public interface NotificationComponent {
    void send(String message);
}
//Koniec Tydzień 3, Wzorzec Composite 3