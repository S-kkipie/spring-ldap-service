package unsa.sistemas.identityservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.models.User;
import unsa.sistemas.identityservice.repositories.IUserRepository;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class SecurityPrincipal {

    private final IUserRepository userRepository;

    public User getLoggedInPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                return userRepository.findByUsername(userDetails.getUsername());
            }
        }

        return null;
    }

    public Collection<?> getLoggedInPrincipalAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                return userDetails.getAuthorities();
            }
        }

        return null;
    }
}
