package com.chesstournament.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> role.equals(auth.getAuthority()));
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isOrganizer() {
        return hasRole("ROLE_ORGANIZER");
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
