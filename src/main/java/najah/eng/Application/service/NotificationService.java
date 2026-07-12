package najah.eng.Application.service;

public interface NotificationService {

    void sendEmail(
            String recipient,
            String subject,
            String message
    );
}