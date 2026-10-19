package com.corebank.service;

import com.corebank.model.User;
import com.corebank.repository.UserRepository;
import com.corebank.util.PasswordUtil;

public class AuthService {

    private UserRepository userRepository;

    public AuthService() {
        userRepository = new UserRepository();
    }



    public User login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username is required.");
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password is required.");
            return null;
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("Invalid username or password.");
            return null;
        }

        // Verify password (SHA-256 or PBKDF2)
        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            System.out.println("Invalid username or password.");
            return null;
        }

        // Migrate legacy SHA-256 password to PBKDF2
        if (PasswordUtil.isLegacyHash(user.getPassword())) {

            String newHash = PasswordUtil.hashPassword(password);

            if (!userRepository.updatePassword(user.getId(), newHash)) {
                System.out.println("Error upgrading password security.");
                return null;
            }

            user.setPassword(newHash);
        }

        System.out.println("Login successful!");

        return user;
    }
}
