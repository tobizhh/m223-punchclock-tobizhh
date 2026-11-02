package ch.zli.m223.service;

import java.util.Set;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.repository.ApplicationUserRepository;

@ApplicationScoped
public class AuthService {

    @Inject
    ApplicationUserRepository userRepository;

    public String login(String username, String password) {
        ApplicationUser user = userRepository.findByUsername(username)
            .filter(u -> BcryptUtil.matches(password, u.getPasswordHash()))
            // Same answer for unknown user and wrong password, so usernames can't be guessed.
            .orElseThrow(() -> new NotAuthorizedException("Invalid username or password.", "Bearer"));
        return generateToken(user);
    }

    private String generateToken(ApplicationUser user) {
        // Issuer and lifespan come from smallrye.jwt.new-token.* in application.properties.
        return Jwt.upn(user.getUsername())
            .subject(user.getId().toString())
            .groups(Set.of(user.getRole()))
            .sign();
    }
}
