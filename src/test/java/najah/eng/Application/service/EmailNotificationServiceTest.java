package najah.eng.Application.service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailNotificationServiceTest {

    @Test
    public void sendEmailPrintsEmailInformation() {
        PrintStream originalOutput =
                System.out;

        ByteArrayOutputStream capturedOutput =
                new ByteArrayOutputStream();

        try {
            System.setOut(
                    new PrintStream(capturedOutput)
            );

            EmailNotificationService service =
                    new EmailNotificationService();

            service.sendEmail(
                    "customer@example.com",
                    "Rental Reminder",
                    "Your rental expires tomorrow."
            );

        } finally {
            System.setOut(originalOutput);
        }

        String result =
                capturedOutput.toString();

        assertTrue(
                result.contains(
                        "Email To: customer@example.com"
                )
        );

        assertTrue(
                result.contains(
                        "Subject: Rental Reminder"
                )
        );

        assertTrue(
                result.contains(
                        "Message: Your rental expires tomorrow."
                )
        );
    }
}