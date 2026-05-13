package tn.esprit.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hash a plain text password using BCrypt
     * @param plainPassword The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Check if a plain text password matches a hashed password
     * @param plainPassword The plain text password to check
     * @param hashedPassword The hashed password from database
     * @return true if passwords match, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            // PHP's password_hash() produces $2y$ prefix hashes; jBCrypt 0.4 only
            // understands $2a$. The two are functionallx y identical, so normalize.
            String normalized = (hashedPassword != null && hashedPassword.startsWith("$2y$"))
                    ? "$2a$" + hashedPassword.substring(4)
                    : hashedPassword;
            return BCrypt.checkpw(plainPassword, normalized);
        } catch (Exception e) {
            return false;
        }
    }
}
