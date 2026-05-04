package ma.swiftrent.service;

import ma.swiftrent.composite.notification.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceFactory {

    /*
    Tydzień 9, Funkcje na tym samym poziomie abstrakcji 3
    createNotificationSystem() wcześniej odpowiadała za wszystko
    teraz wywołuje tylko funkcje, które odpowiadają za szczegóły,
    tym samym uzyskując separację abstrakcji: ogólnie co robimy,
    za pomocą jakich kroków to osiągniemy i szczegóły tych kroków
     */
    public NotificationComponent createNotificationSystem() {
        NotificationComponents components = createNotificationComponents();

        NotificationGroup adminGroup = createAdminGroup(components);
        NotificationGroup userGroup = createUserGroup(components);

        return createSystemGroup(adminGroup, userGroup);
    }

    private NotificationComponents createNotificationComponents() {
        return new NotificationComponents(
                new EmailNotification(),
                new SmsNotification(),
                new LogNotification()
        );
    }

    private NotificationGroup createAdminGroup(NotificationComponents components) {
        NotificationGroup adminGroup = new NotificationGroup("Admin");

        adminGroup.add(components.email());
        adminGroup.add(components.log());

        return adminGroup;
    }

    private NotificationGroup createUserGroup(NotificationComponents components) {
        NotificationGroup userGroup = new NotificationGroup("User");

        userGroup.add(components.email());
        userGroup.add(components.sms());

        return userGroup;
    }

    private NotificationGroup createSystemGroup(
            NotificationGroup adminGroup,
            NotificationGroup userGroup
    ) {
        NotificationGroup systemGroup = new NotificationGroup("System");

        systemGroup.add(adminGroup);
        systemGroup.add(userGroup);

        return systemGroup;
    }

    private record NotificationComponents(
            NotificationComponent email,
            NotificationComponent sms,
            NotificationComponent log
    ) {
    }
    //Koniec, Tydzień 9, Funkcje na jednym poziomie abstrakcji 3
}
