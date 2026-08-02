package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Provides file-based persistence operations for manager accounts.
 *
 * <p>The repository reads manager usernames and passwords from a
 * comma-separated text file and searches for matching credentials.</p>
 */
public class ManagerRepository {

    /**
     * The path of the text file containing manager credentials.
     */
    private final Path filePath;

    /**
     * Creates a manager repository that uses the default managers file.
     *
     * <p>The default file is located at
     * {@code src/main/resources/managers.txt}.</p>
     */
    public ManagerRepository() {
        this(
                Path.of(
                        System.getProperty("user.dir"),
                        "src",
                        "main",
                        "resources",
                        "managers.txt"
                )
        );
    }

    /**
     * Creates a manager repository using a specified file.
     *
     * @param filePath the path of the file containing manager credentials
     */
    public ManagerRepository(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Searches for a manager whose stored credentials match
     * the supplied username and password.
     *
     * <p>Blank credentials, missing files, malformed records, and
     * file-reading errors result in a {@code null} return value.</p>
     *
     * @param username the username entered by the manager
     * @param password the password entered by the manager
     * @return the matching manager, or {@code null} when no valid
     *         matching account is found
     */
    public Manager findByCredentials(
            String username,
            String password) {

        if (username == null ||
                username.isBlank() ||
                password == null ||
                password.isBlank()) {

            return null;
        }

        if (!Files.exists(filePath)) {
            return null;
        }

        try {
            List<String> lines =
                    Files.readAllLines(filePath);

            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }

                String[] parts =
                        line.split(",", -1);

                if (parts.length != 2) {
                    continue;
                }

                String savedUsername =
                        parts[0].trim();

                String savedPassword =
                        parts[1].trim();

                if (savedUsername.equals(
                        username.trim()
                ) &&
                        savedPassword.equals(
                                password.trim()
                        )) {

                    return new Manager(
                            savedUsername,
                            savedPassword
                    );
                }
            }

        } catch (IOException e) {
            return null;
        }

        return null;
    }
}