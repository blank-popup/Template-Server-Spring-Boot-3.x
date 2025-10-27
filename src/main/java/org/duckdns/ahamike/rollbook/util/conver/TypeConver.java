package org.duckdns.ahamike.rollbook.util.conver;

import java.util.List;
import java.util.Map;

public class TypeConver {
    public static Map<String, Object> object2Map(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Map) {
            Map<?, ?> rawMap = (Map<?, ?>) object;
            boolean canCast = true;
            for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                if (entry.getKey() instanceof String == false) {
                    canCast = false;
                    break;
                }
            }
            if (canCast) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) object;
                return map;
            }
        }

        return null;
    }

    public static List<Object> object2List(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) object;
            return list;
        }

        return null;
    }
}
