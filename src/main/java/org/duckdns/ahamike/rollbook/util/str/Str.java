package org.duckdns.ahamike.rollbook.util.str;

public class Str {
    public static String truncate(String str, int maxSize) {
        return (str != null && str.length() > maxSize) ? str.substring(0, maxSize) : str;
    }

    public static String truncate(String str, int maxSize, String suffix) {
        int length = suffix != null ? suffix.length() : 0;
        if (length >= maxSize) {
            throw new IllegalArgumentException("Suffix length must be less than maxSize");
        }
        int maxSizeAdjusted = maxSize - length;
        return (str != null && str.length() > maxSizeAdjusted) ? str.substring(0, maxSizeAdjusted) + (suffix == null ? "" : suffix) : str;
    }
}
