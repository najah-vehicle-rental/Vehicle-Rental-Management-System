package najah.eng.Application.service;

import najah.eng.Application.Domain.Manager;
import najah.eng.Application.Persistence.ManagerRepository;

public class AuthService {

    private final ManagerRepository managerRepository;
    private boolean loggedIn;

    public AuthService() {
        this(new ManagerRepository());
    }

    public AuthService(
            ManagerRepository managerRepository) {

        this.managerRepository =
                managerRepository;

        loggedIn = false;
    }

    public boolean login(
            String username,
            String password) {

        Manager manager =
                managerRepository.findByCredentials(
                        username,
                        password
                );

        loggedIn = manager != null;

        return loggedIn;
    }

    public void logout() {
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}