package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ManagerRepository {

    private static final String FILE_PATH = "C:\\Users\\admin\\Desktop\\Vehicle Rental Management System\\Vehicle-Rental-Management-System\\src\\managers.txt";

    public Manager findByUsername(String username) {

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 2 &&
                        parts[0].trim().equals(username.trim()) &&
                        parts[1].trim() != null) {

                    return new Manager(parts[0].trim(), parts[1].trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}