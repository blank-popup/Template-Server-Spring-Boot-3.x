package org.duckdns.ahamike.rollbook.util.json;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {
    public static JsonNode object2JsonNode(ObjectMapper mapper, Object object) {
        if (mapper == null || object == null) {
            return null;
        }
        try {
            return mapper.valueToTree(object);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String object2JsonString(ObjectMapper mapper, Object object) {
        if (mapper == null || object == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            return object.toString();
            // return String.valueOf(obj);
        }
    }

    public static Map<String, Object> object2Map(ObjectMapper mapper, Object object) {
        if (mapper == null || object == null) {
            return null;
        }
        try {
            return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static List<Object> object2List(ObjectMapper mapper, Object object) {
        if (mapper == null || object == null) {
            return null;
        }
        try {
            return mapper.convertValue(object, new TypeReference<List<Object>>() {});
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Object map2Object(ObjectMapper mapper, Map<String, Object> map, Class<?> clazz) {
        if (mapper == null || map == null) {
            return null;
        }
        try {
            return mapper.convertValue(map, clazz);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void sanitize(JsonNode node, String keyFinding, String mask) {
        if (node == null) {
            return;
        }
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            // Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields();
            Iterator<Map.Entry<String, JsonNode>> it = objectNode.properties().iterator();
            while (it.hasNext() == true) {
                Map.Entry<String, JsonNode> e = it.next();
                String key = e.getKey();
                JsonNode child = e.getValue();
                if (key != null && key.equals(keyFinding) == true) {
                    if (child.isTextual() == true) {
                        objectNode.put(key, mask);
                    }
                } else {
                    sanitize(child, keyFinding, mask);
                }
            }
        } else if (node.isArray() == true) {
            ArrayNode array = (ArrayNode) node;
            for (JsonNode child : array) {
                sanitize(child, keyFinding, mask);
            }
        }
    }
}
