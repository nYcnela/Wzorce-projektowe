package ma.swiftrent.service;

import ma.swiftrent.composite.notification.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceFactory {

    public NotificationComponent createNotificationSystem() {

        NotificationComponent email = new EmailNotification();
        NotificationComponent sms = new SmsNotification();
        NotificationComponent log = new LogNotification();

        NotificationGroup adminGroup = new NotificationGroup("Admin");
        adminGroup.add(email);
        adminGroup.add(log);

        NotificationGroup userGroup = new NotificationGroup("User");
        userGroup.add(email);
        userGroup.add(sms);

        NotificationGroup systemGroup = new NotificationGroup("System");
        systemGroup.add(adminGroup);
        systemGroup.add(userGroup);

        return systemGroup;
    }
}
