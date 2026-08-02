package najah.eng.Application.service;

/**
 * Provides a console-based implementation of email notifications.
 *
 * <p>The service simulates sending an email by printing the recipient,
 * subject, and message to standard output.</p>
 */
public class EmailNotificationService
        implements NotificationService {

    /**
     * Prints an email notification to the console.
     *
     * @param recipient the recipient email address
     * @param subject   the email subject
     * @param message   the email message body
     */
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