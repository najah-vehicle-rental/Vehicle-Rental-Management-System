package najah.eng.Application.Domain;

/**
 * Represents a manager account that can authenticate and use
 * protected vehicle-rental management operations.
 */
public class Manager {

    /**
     * The username used by the manager to log in.
     */
    private String username;

    /**
     * The password used by the manager to log in.
     */
    private String password;

    /**
     * Creates a manager with the supplied login credentials.
     *
     * @param username the manager username
     * @param password the manager password
     */
    public Manager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the manager username.
     *
     * @return the manager username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the manager password.
     *
     * @return the manager password
     */
    public String getPassword() {
        return password;
    }
}