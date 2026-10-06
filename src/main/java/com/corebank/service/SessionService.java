package com.corebank.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.corebank.model.User;

public class SessionService {

    private static final Map<String, User> sessions = new HashMap<>();

    // ------------------------------ CREATE SESSION ------------------------------

    public static String createSession(User user) {

        String token = UUID.randomUUID().toString();
        sessions.put(token, user);

        return token;
    }

    // ------------------------------ GET USER ------------------------------
    public static User getUser(String token) {

        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        return sessions.get(token);
    }

    // ------------------------------ REMOVE SESSION ------------------------------
    public static boolean removeSession(String token) {

        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        return sessions.remove(token) != null;
    }
}