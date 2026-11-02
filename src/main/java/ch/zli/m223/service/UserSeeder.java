package ch.zli.m223.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.repository.ApplicationUserRepository;

/**
 * Creates the sample users on startup if they don't exist yet.
 * Only for the exercise: real passwords never belong in source code.
 */
@ApplicationScoped
public class UserSeeder {

    @Inject
    ApplicationUserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        createIfMissing("admin", "admin123", "Admin");
        createIfMissing("user", "user123", "User");
    }

    private void createIfMissing(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setPasswordHash(BcryptUtil.bcryptHash(password));
        user.setRole(role);
        userRepository.persist(user);
    }
}
