package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ManagerRepository {

    private final Path filePath;

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

    public ManagerRepository(Path filePath) {
        this.filePath = filePath;
    }

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