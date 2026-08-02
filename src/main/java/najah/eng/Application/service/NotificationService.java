package najah.eng.Application.service;

/**
 * Defines the operation required to send customer email notifications.
 */
public interface NotificationService {

    /**
     * Sends an email notification.
     *
     * @param recipient the recipient email address
     * @param subject   the email subject
     * @param message   the email message body
     */
    void sendEmail(
            String recipient,
            String subject,
            String message
    );
}