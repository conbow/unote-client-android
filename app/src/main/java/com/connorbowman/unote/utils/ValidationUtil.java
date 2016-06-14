package com.connorbowman.unote.utils;

import java.util.regex.Pattern;

public class ValidationUtil {

    public static final int PASSWORD_LENGTH_MIN = 8;

    public static boolean isEmail(String input) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    public static boolean isPassword(String input) {
        return !(input.length() < PASSWORD_LENGTH_MIN || !containsNumber(input) || !containsUpperCase(input));
    }

    public static boolean containsNumber(String input) {
        return Pattern.compile("[0-9]").matcher(input).find();
    }

    public static boolean containsUpperCase(String input) {
        return !input.equals(input.toLowerCase());
    }
}
