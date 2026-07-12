package najah.eng.Application.service;

public class EmailNotificationService implements NotificationService {

    @Override
    public void sendEmail(
            String recipient,
            String subject,
            String message) {

        System.out.println("Email To: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
    }
}