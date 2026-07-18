package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Manager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ManagerRepositoryTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void validCredentialsFindManager()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "managers.txt"
                );

        Files.writeString(
                file,
                "admin,1234\n"
        );

        ManagerRepository repository =
                new ManagerRepository(file);

        Manager manager =
                repository.findByCredentials(
                        "admin",
                        "1234"
                );

        assertNotNull(manager);
    }

    @Test
    public void invalidPasswordReturnsNull()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "managers.txt"
                );

        Files.writeString(
                file,
                "admin,1234\n"
        );

        ManagerRepository repository =
                new ManagerRepository(file);

        assertNull(
                repository.findByCredentials(
                        "admin",
                        "wrong"
                )
        );
    }

    @Test
    public void invalidRecordsAreIgnored()
            throws IOException {

        Path file =
                tempDirectory.resolve(
                        "managers.txt"
                );

        Files.writeString(
                file,
                "invalid\n" +
                        "admin,1234\n"
        );

        ManagerRepository repository =
                new ManagerRepository(file);

        assertNotNull(
                repository.findByCredentials(
                        "admin",
                        "1234"
                )
        );
    }

    @Test
    public void missingFileReturnsNull() {
        Path file =
                tempDirectory.resolve(
                        "missing.txt"
                );

        ManagerRepository repository =
                new ManagerRepository(file);

        assertNull(
                repository.findByCredentials(
                        "admin",
                        "1234"
                )
        );
    }
}