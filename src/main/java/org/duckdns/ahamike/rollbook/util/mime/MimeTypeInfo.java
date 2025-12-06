package org.duckdns.ahamike.rollbook.util.mime;

import java.util.Map;

public class MimeTypeInfo {
    private final String type;
    private final String subType;
    private final Map<String, String> parameters;

    public MimeTypeInfo(String type, String subType, Map<String, String> parameters) {
        this.type = type;
        this.subType = subType;
        this.parameters = parameters;
    }

    public String getType() { return type; }
    public String getSubType() { return subType; }
    public Map<String, String> getParameters() { return parameters; }

    @Override
    public String toString() {
        return "MimeTypeInfo{" +
                "type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
