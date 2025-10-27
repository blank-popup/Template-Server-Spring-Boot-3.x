package org.duckdns.ahamike.rollbook.util.validator;

public class NullString {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty() == true;
    }

    public static boolean isNullOrNotEmpty(String str) {
        return str == null || str.isEmpty() == false;
    }

    public static boolean isNotNullAndEmpty(String str) {
        return str != null && str.isEmpty() == true;
    }

    public static boolean isNotNullAndNotEmpty(String str) {
        return str != null && str.isEmpty() == false;
    }

    public static boolean isNullOrWhitespace(String str) {
        return str == null || str.trim().isEmpty() == true;
    }

    public static boolean isNullOrNotWhitespace(String str) {
        return str == null || str.trim().isEmpty() == false;
    }

    public static boolean isNotNullAndWhitespace(String str) {
        return str != null && str.trim().isEmpty() == true;
    }

    public static boolean isNotNullAndNotWhitespace(String str) {
        return str != null && str.trim().isEmpty() == false;
    }
}
