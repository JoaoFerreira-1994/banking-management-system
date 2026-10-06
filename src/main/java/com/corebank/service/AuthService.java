package com.corebank.service;

import com.corebank.model.User;
import com.corebank.repository.UserRepository;
import com.corebank.util.PasswordUtil;

public class AuthService {

    private UserRepository userRepository;

    public AuthService() {
        userRepository = new UserRepository();
    }


    // ------------------------------ LOGIN ------------------------------

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

        String hashedPassword =
            PasswordUtil.hashPassword(password);

        if (!hashedPassword.equals(user.getPassword())) {
            System.out.println("Invalid username or password.");
            return null;
        }

        System.out.println("Login successful!");

        return user;
    }
}
