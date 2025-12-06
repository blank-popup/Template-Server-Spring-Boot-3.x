package org.duckdns.ahamike.rollbook.config.logging.setting;

import java.time.LocalDateTime;

import org.duckdns.ahamike.rollbook.config.logging.LoggingConfigService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogParameter {
    private ApiHistoryService apiHistoryService;
    private LoggingConfigService loggingConfigService;
    private ObjectMapper mapper;
    private String signUpUri;
    private String signInUri;
    private int maxRequestBodySize;
    private int maxResponseBodySize;

    private Object requestObjectBody;
    private Object responseObjectBody;

    private String username;
    private String method;
    private String uri;
    private String pathVariable;
    private String requestParam;
    private String requestPartFile;
    private String requestPartParam;
    private String ip;
    private String userAgent;
    private String requestBody;

    private Integer httpStatusValue;
    private LocalDateTime createdAt;
    private Long duration;
    private String responseBody;

    public LogParameter(ApiHistoryService apiHistoryService, LoggingConfigService loggingConfigService, ObjectMapper mapper, String signUpUri, String signInUri, int maxRequestBodySize, int maxResponseBodySize, Object requestObjectBody, Object responseObjectBody) {
        this.apiHistoryService = apiHistoryService;
        this.loggingConfigService = loggingConfigService;
        this.mapper = mapper;
        this.signUpUri = signUpUri;
        this.signInUri = signInUri;
        this.maxRequestBodySize = maxRequestBodySize;
        this.maxResponseBodySize = maxResponseBodySize;

        this.requestObjectBody = requestObjectBody;
        this.responseObjectBody = responseObjectBody;
    }

    public LogParameter(ApiHistoryService apiHistoryService, LoggingConfigService loggingConfigService, ObjectMapper mapper, String signUpUri, String signInUri, int maxRequestBodySize, int maxResponseBodySize, Object requestObjectBody) {
        this(apiHistoryService, loggingConfigService, mapper, signUpUri, signInUri, maxRequestBodySize, maxResponseBodySize, requestObjectBody, null);
    }

    public LogParameter(ApiHistoryService apiHistoryService, LoggingConfigService loggingConfigService, ObjectMapper mapper, String signUpUri, String signInUri, int maxRequestBodySize, int maxResponseBodySize) {
        this(apiHistoryService, loggingConfigService, mapper, signUpUri, signInUri, maxRequestBodySize, maxResponseBodySize, null, null);
    }
}
