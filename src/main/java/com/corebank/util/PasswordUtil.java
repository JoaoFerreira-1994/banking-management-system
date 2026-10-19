
package com.corebank.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final int ITERATIONS = 600000;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 256;

    // ------------------------------ HASH PASSWORD ------------------------------

    public static String hashPassword(String password) {

        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATIONS,
            HASH_LENGTH
        );

        try {
            SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return ITERATIONS + ":" +
                Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Error hashing password.", e);
        } finally {
            spec.clearPassword();
        }
    }

    // ------------------------------ VERIFY PASSWORD ------------------------------

    public static boolean verifyPassword(
            String password, String storedHash) {

        if (password == null || storedHash == null) {
            return false;
        }

        // Legacy SHA-256
        if (storedHash.matches("[0-9a-fA-F]{64}")) {
            return MessageDigest.isEqual(
                sha256(password).getBytes(StandardCharsets.UTF_8),
                storedHash.toLowerCase().getBytes(StandardCharsets.UTF_8)
            );
        }

        // PBKDF2 with salt
        try {
            String[] parts = storedHash.split(":");

            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);

            if (iterations < 1 || iterations > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[2]);

            if (salt.length != SALT_LENGTH ||
                expectedHash.length != HASH_LENGTH / 8) {
                return false;
            }

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                expectedHash.length * 8
            );

            try {
                SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                byte[] actualHash =
                    factory.generateSecret(spec).getEncoded();

                return MessageDigest.isEqual(actualHash, expectedHash);

            } finally {
                spec.clearPassword();
            }

        } catch (Exception e) {
            return false;
        }
    }

    // ------------------------------ LEGACY SHA-256 ------------------------------

    private static String sha256(String password) {

        try {
            MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder result = new StringBuilder();

            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }

            return result.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error hashing password.", e);
        }
    }

    // ------------------------------ CHECK LEGACY HASH ------------------------------

    public static boolean isLegacyHash(String storedHash) {

        return storedHash != null &&
            storedHash.matches("[0-9a-fA-F]{64}");
    }
}

