package com.bupt.utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class PasswordUtils {
    public static String encodePassword(String plainPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(plainPassword);
    }

    public static boolean matchPassword(String plainPassword, String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(plainPassword, encodedPassword);
    }

}
