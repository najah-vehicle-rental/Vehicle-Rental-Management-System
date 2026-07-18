package najah.eng.Application.observer;

import najah.eng.Application.Domain.Rental;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditLogObserverTest {

    private PrintStream originalOutput;
    private ByteArrayOutputStream output;

    @BeforeEach
    public void setUp() {
        originalOutput = System.out;
        output = new ByteArrayOutputStream();

        System.setOut(
                new PrintStream(output)
        );
    }

    @AfterEach
    public void cleanUp() {
        System.setOut(originalOutput);
    }

    @Test
    public void nullEventProducesNoOutput() {
        AuditLogObserver observer =
                new AuditLogObserver();

        observer.update(null);

        assertTrue(
                output.toString().isBlank()
        );
    }

    @Test
    public void eventWithoutRentalProducesNoOutput() {
        AuditLogObserver observer =
                new AuditLogObserver();

        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RENTED,
                        null
                );

        observer.update(event);

        assertTrue(
                output.toString().isBlank()
        );
    }

    @Test
    public void validEventProducesAuditOutput() {
        AuditLogObserver observer =
                new AuditLogObserver();

        Rental rental = new Rental(
                "1",
                "Fadi",
                "fadi@example.com",
                5,
                LocalDate.of(2026, 7, 23),
                "Active"
        );

        RentalEvent event =
                new RentalEvent(
                        RentalEventType.RENTED,
                        rental
                );

        observer.update(event);

        String result =
                output.toString();

        assertTrue(
                result.contains(
                        "Audit Event: RENTED"
                )
        );

        assertTrue(
                result.contains(
                        "Audit Vehicle ID: 1"
                )
        );

        assertTrue(
                result.contains(
                        "Audit Customer: Fadi"
                )
        );
    }
}