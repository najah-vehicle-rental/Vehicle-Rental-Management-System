package najah.eng.Application.service;

import najah.eng.Application.Domain.Manager;
import najah.eng.Application.Persistence.ManagerRepository;

public class AuthService {

    private ManagerRepository managerRepository = new ManagerRepository();

    public boolean login(String username, String password) {

        Manager manager = managerRepository.findByUsername(username);

        if (manager == null) {
            return false;
        }

        return manager.getPassword().equals(password);
    }
}