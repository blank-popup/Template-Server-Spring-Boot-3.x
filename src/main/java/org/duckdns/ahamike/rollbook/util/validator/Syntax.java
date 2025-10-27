package org.duckdns.ahamike.rollbook.util.validator;

import java.util.regex.Pattern;

public class Syntax {

    public static boolean isEmail(String str) {
        if (str == null) return false;
        String regex = "^[\\w.-]+@[\\w.-]+\\.[\\w.-]{2,}$";
        return Pattern.matches(regex, str);
    }

    public static boolean isInteger(String str) {
        if (str == null) return false;
        String regex = "^-?\\d+$";
        return Pattern.matches(regex, str);
    }

    public static boolean isFloat(String str) {
        if (str == null) return false;
        String regex = "^-?\\d*\\.\\d+$";
        return Pattern.matches(regex, str);
    }

    public static boolean isAlphabet(String str) {
        if (str == null) return false;
        String regex = "^[a-zA-Z]+$";
        return Pattern.matches(regex, str);
    }

    public static boolean isAlphanumeric(String str) {
        if (str == null) return false;
        String regex = "^[a-zA-Z0-9]+$";
        return Pattern.matches(regex, str);
    }

    public static boolean isHexadecimal(String str) {
        if (str == null) return false;
        String regex = "^0[xX][0-9a-fA-F]+$";
        return Pattern.matches(regex, str);
    }
}