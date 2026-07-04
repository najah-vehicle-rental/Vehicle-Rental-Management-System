package najah.eng.Application.Persistence;

import najah.eng.Application.Domain.Vehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class VehicleRepository {

    public ArrayList<Vehicle> findAvailableVehicles() {

        ArrayList<Vehicle> vehicles = new ArrayList<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("vehicles.txt");

        if (inputStream == null) {
            return vehicles;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 3 && parts[2].trim().equalsIgnoreCase("Available")) {
                    vehicles.add(new Vehicle(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vehicles;
    }
}