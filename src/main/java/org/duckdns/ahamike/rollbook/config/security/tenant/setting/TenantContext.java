package org.duckdns.ahamike.rollbook.config.security.tenant.setting;

import java.util.Arrays;
import java.util.List;

public class TenantContext {

    private static final ThreadLocal<String> CURRENT_BELONG = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    private static final String board = "board";
    private static final List<String> belongs = Arrays.asList(
        "writing",
        "sheet",
        "paint"
    );

    public static void setCurrentBelong(String belongName) {
        CURRENT_BELONG.set(belongName);
    }

    public static void setCurrentTenant(String tenantName) {
        CURRENT_TENANT.set(tenantName);
    }

    public static String getCurrentBelong() {
        return CURRENT_BELONG.get();
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_BELONG.remove();
        CURRENT_TENANT.remove();
    }

    public static String getBoard() {
        return board;
    }

    public static boolean isBelongContained(String belong) {
        return belongs.contains(belong);
    }

    public static boolean isBoardPath(String[] partUri) {
        if (partUri.length < 6)                                 return false; 
        if (getBoard().equalsIgnoreCase(partUri[3]) == false)   return false;
        if (isBelongContained(partUri[4]) == false)             return false;
        if (partUri[5] == null)                                 return false;
        if (partUri[5].isBlank() == true)                       return false;

        return true;
    }

    public static void setBoardBelongTenant(String uri) {
        String[] partUri = uri.split("/");
        if (isBoardPath(partUri) == true) {
            setCurrentBelong(partUri[4]);
            setCurrentTenant(partUri[5]);
        }
    }

    public static String getBoardTenant(String uri) {
        String[] partUri = uri.split("/");
        if (partUri.length < 6 
                || getBoard().equalsIgnoreCase(partUri[3]) == false
                || isBelongContained(partUri[4]) == false) {
            return null;
        }

        return partUri[5];
    }
}
