package najah.eng.Application.service;

import najah.eng.Application.Domain.Manager;
import najah.eng.Application.Persistence.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private ManagerRepository managerRepository;
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        managerRepository =
                mock(ManagerRepository.class);

        authService =
                new AuthService(
                        managerRepository
                );
    }

    @Test
    public void validCredentialsLoginSuccessfully() {
        Manager manager =
                new Manager(
                        "admin",
                        "1234"
                );

        when(
                managerRepository.findByCredentials(
                        "admin",
                        "1234"
                )
        ).thenReturn(manager);

        assertTrue(
                authService.login(
                        "admin",
                        "1234"
                )
        );

        assertTrue(authService.isLoggedIn());
    }

    @Test
    public void invalidCredentialsFailLogin() {
        when(
                managerRepository.findByCredentials(
                        "admin",
                        "wrong"
                )
        ).thenReturn(null);

        assertFalse(
                authService.login(
                        "admin",
                        "wrong"
                )
        );

        assertFalse(authService.isLoggedIn());
    }

    @Test
    public void logoutEndsSession() {
        Manager manager =
                new Manager(
                        "admin",
                        "1234"
                );

        when(
                managerRepository.findByCredentials(
                        "admin",
                        "1234"
                )
        ).thenReturn(manager);

        authService.login(
                "admin",
                "1234"
        );

        authService.logout();

        assertFalse(authService.isLoggedIn());
    }

    @Test
    public void failedLoginClearsPreviousSession() {
        Manager manager =
                new Manager(
                        "admin",
                        "1234"
                );

        when(
                managerRepository.findByCredentials(
                        "admin",
                        "1234"
                )
        ).thenReturn(manager);

        when(
                managerRepository.findByCredentials(
                        "admin",
                        "wrong"
                )
        ).thenReturn(null);

        authService.login(
                "admin",
                "1234"
        );

        authService.login(
                "admin",
                "wrong"
        );

        assertFalse(authService.isLoggedIn());
    }
}