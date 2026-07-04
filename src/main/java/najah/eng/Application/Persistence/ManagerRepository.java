package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ManagerRepository {

    public Manager findByUsername(String username) {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("managers.txt");

        if (inputStream == null) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 2 && parts[0].trim().equals(username.trim())) {
                    return new Manager(parts[0].trim(), parts[1].trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}