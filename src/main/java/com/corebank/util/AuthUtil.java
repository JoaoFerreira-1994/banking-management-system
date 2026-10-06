package com.corebank.util;

import com.corebank.model.User;
import com.corebank.service.SessionService;

public class AuthUtil {

    // ------------------------------ GET AUTHENTICATED USER ------------------------------
    public static User getAuthenticatedUser(String authorization) {

        if (authorization == null ||
            !authorization.startsWith("Bearer ")) {

            return null;
        }

        String token = authorization.substring(7);

        if (token.trim().isEmpty()) {
            return null;
        }
        return SessionService.getUser(token);
    }


    // ------------------------------ CHECK ROLE ------------------------------
    public static boolean hasRole(User user, String role) {
        if (user == null ||
            user.getRole() == null) {

            return false;
        }
        return role.equalsIgnoreCase(user.getRole());
    }

    // ------------------------------ CHECK EMPLOYEE ------------------------------
    public static boolean isEmployee(User user) {
        return hasRole(user, "EMPLOYEE");
    }


    // ------------------------------ CHECK CLIENT ------------------------------
    public static boolean isClient(User user) {
        return hasRole(user, "CLIENT");
    }
}