package org.duckdns.ahamike.rollbook.config.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.duckdns.ahamike.rollbook.util.str.Str;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoRequestParam {
    private Map<String, String> pathVariables = new HashMap<>();
    private Map<String, String[]> requestParams = new HashMap<>();
    private List<FileMeta> requestPartsFile = new ArrayList<>();
    private Map<String, String[]> requestPartsParam = new HashMap<>();
    private Object requestBody;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileMeta {
        private String fieldName;
        private String originalFilename;
        private String contentType;
    }

    @Override
    public String toString() {
        return "InfoRequestParam{"
                + "pathVariables=" + pathVariables
                + ", requestParams=" + Str.map2String(requestParams)
                + ", requestPartsFile=" + requestPartsFile
                + ", requestPartsParam=" + Str.map2String(requestPartsParam)
                + ", requestBody=" + requestBody
                + "}";
    }
}
