package org.duckdns.ahamike.rollbook.config.logging;

import java.time.LocalDateTime;

import org.duckdns.ahamike.rollbook.config.logging.setting.ServiceLoggingConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogParameter {
    private ServiceApiHistory apiHistoryService;
    private ServiceLoggingConfig loggingConfigService;
    private ObjectMapper mapper;
    private String uriSignUp;
    private String uriSignIn;
    private int maxRequestBodySize;
    private int maxResponseBodySize;

    private Object requestObjectBody;
    private Object responseObjectBody;

    private String username;
    private String method;
    private String uri;
    private String ip;
    private String userAgent;
    private String requestBody;

    private Integer httpStatusValue;
    private LocalDateTime createdAt;
    private Long duration;
    private String responseBody;

    public LogParameter(ServiceApiHistory apiHistoryService, ServiceLoggingConfig loggingConfigService, ObjectMapper mapper, String uriSignUp, String uriSignIn, int maxRequestBodySize, int maxResponseBodySize, Object requestObjectBody, Object responseObjectBody) {
        this.apiHistoryService = apiHistoryService;
        this.loggingConfigService = loggingConfigService;
        this.mapper = mapper;
        this.uriSignUp = uriSignUp;
        this.uriSignIn = uriSignIn;
        this.maxRequestBodySize = maxRequestBodySize;
        this.maxResponseBodySize = maxResponseBodySize;

        this.requestObjectBody = requestObjectBody;
        this.responseObjectBody = responseObjectBody;
    }

    public LogParameter(ServiceApiHistory apiHistoryService, ServiceLoggingConfig loggingConfigService, ObjectMapper mapper, String uriSignUp, String uriSignIn, int maxRequestBodySize, int maxResponseBodySize, Object requestObjectBody) {
        this(apiHistoryService, loggingConfigService, mapper, uriSignUp, uriSignIn, maxRequestBodySize, maxResponseBodySize, requestObjectBody, null);
    }

    public LogParameter(ServiceApiHistory apiHistoryService, ServiceLoggingConfig loggingConfigService, ObjectMapper mapper, String uriSignUp, String uriSignIn, int maxRequestBodySize, int maxResponseBodySize) {
        this(apiHistoryService, loggingConfigService, mapper, uriSignUp, uriSignIn, maxRequestBodySize, maxResponseBodySize, null, null);
    }
}
