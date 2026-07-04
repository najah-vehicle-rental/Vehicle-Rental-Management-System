package najah.eng.Application.service;

import najah.eng.Application.Domain.Manager;
import najah.eng.Application.Persistence.ManagerRepository;

public class AuthService {

    private ManagerRepository managerRepository = new ManagerRepository();
    private boolean loggedIn = false;

    public boolean login(String username, String password) {
        Manager manager = managerRepository.findByUsername(username);

        if (manager == null) {
            loggedIn = false;
            return false;
        }

        loggedIn = manager.getPassword().equals(password);
        return loggedIn;
    }

    public void logout() {
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}