package najah.eng.Application.service;

import najah.eng.Application.Domain.Manager;
import najah.eng.Application.Persistence.ManagerRepository;

/**
 * Provides authentication operations for manager accounts.
 *
 * <p>The service checks the supplied username and password through
 * {@link ManagerRepository} and stores the current login state.</p>
 */
public class AuthService {

    /**
     * Repository used to search for manager credentials.
     */
    private final ManagerRepository managerRepository;

    /**
     * Indicates whether a manager is currently logged in.
     */
    private boolean loggedIn;

    /**
     * Creates an authentication service using the default
     * manager repository.
     */
    public AuthService() {
        this(new ManagerRepository());
    }

    /**
     * Creates an authentication service using a supplied repository.
     *
     * @param managerRepository repository used to access manager accounts
     */
    public AuthService(
            ManagerRepository managerRepository) {

        this.managerRepository =
                managerRepository;

        loggedIn = false;
    }

    /**
     * Attempts to authenticate a manager.
     *
     * <p>The login state becomes {@code true} only when a manager
     * with matching credentials is found.</p>
     *
     * @param username the manager username
     * @param password the manager password
     * @return {@code true} when the credentials are valid;
     *         otherwise {@code false}
     */
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

    /**
     * Logs out the currently authenticated manager.
     */
    public void logout() {
        loggedIn = false;
    }

    /**
     * Checks whether a manager is currently logged in.
     *
     * @return {@code true} when a manager is logged in;
     *         otherwise {@code false}
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
}