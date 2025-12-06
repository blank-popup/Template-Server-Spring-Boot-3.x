package org.duckdns.ahamike.rollbook.config.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.duckdns.ahamike.rollbook.config.response.BusinessException;
import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.response.ReturnCode;
import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointOrder;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/logging")
@Slf4j
public class LoggingConfigController {

    private final LoggingConfigService loggingConfigService;
    private final List<String> loggerNames = Arrays.asList(
        "org.apache.catalina.startup.DigesterFactory",
        "org.apache.catalina.util.LifecycleBase",
        "org.apache.coyote.http11.Http11NioProtocol",
        "org.apache.sshd.common.util.SecurityUtils",
        "org.apache.tomcat.util.net.NioSelectorPool",
        "org.eclipse.jetty.util.component.AbstractLifeCycle",
        "org.hibernate.validator.internal.util.Version",
        "org.springframework.boot.actuate.endpoint.jmx",

        "org.springframework",
        "org.duckdns.ahamike",
        "org.hibernate.SQL",
        "com.zaxxer.hikari",
        "org.hibernate.ype.descriptor.sql",
        "org.hibernate.orm.jdbc.bind"
    );

    @EndpointOrder(value0 = 600, value1 = 1100)
    @PostMapping("/aop")
    public ResponseEntity<?> setLogingAop(
            @RequestParam(name = "requestOnOff", required = false) String request,
            @RequestParam(name = "responseOnOff", required = false) String response,
            @RequestParam(name = "databaseOnOff", required = false) String database) {
        if (request != null) {
            if ("on".equalsIgnoreCase(request)) {
                loggingConfigService.enableRequest();
            } else if ("off".equalsIgnoreCase(request)) {
                loggingConfigService.disableRequest();
            } else {
                loggingConfigService.enableRequest();
            }
        }
        if (response != null) {
            if ("on".equalsIgnoreCase(response)) {
                loggingConfigService.enableResponse();
            } else if ("off".equalsIgnoreCase(response)) {
                loggingConfigService.disableResponse();
            } else {
                loggingConfigService.enableResponse();
            }
        }
        if (database != null) {
            if ("on".equalsIgnoreCase(database)) {
                loggingConfigService.enableDatabase();
            } else if ("off".equalsIgnoreCase(database)) {
                loggingConfigService.disableDatabase();
            } else {
                loggingConfigService.enableDatabase();
            }
        }

        return getLoggingAop();
    }

    @EndpointOrder(value0 = 600, value1 = 1200)
    @GetMapping("/aop")
    public ResponseEntity<?> getLoggingAop() {
        LoggingAopResponse response = new LoggingAopResponse();
        response.setIsLoggingRequest(loggingConfigService.isEnabledRequest());
        response.setIsLoggingResponse(loggingConfigService.isEnabledResponse());
        response.setIsLoggingDatabase(loggingConfigService.isEnabledDatabase());

        ReturnCode code = ReturnCode.OK;
        return buildResponseEntity(code, response);
    }

    @EndpointOrder(value0 = 600, value1 = 2100)
    @PostMapping("/level/{loggerName}")
    public ResponseEntity<?> setLoggingLevel(@PathVariable(name = "loggerName") String loggerName, @RequestParam(name = "level") String level) {
        if ("trace".equalsIgnoreCase(level)) {
            loggingConfigService.setLoggingLevel(loggerName, LogLevel.TRACE);
        }
        else if ("debug".equalsIgnoreCase(level)) {
            loggingConfigService.setLoggingLevel(loggerName, LogLevel.DEBUG);
        }
        else if ("info".equalsIgnoreCase(level)) {
            loggingConfigService.setLoggingLevel(loggerName, LogLevel.INFO);
         }
        else if ("warn".equalsIgnoreCase(level)) {
            loggingConfigService.setLoggingLevel(loggerName, LogLevel.WARN);
        }
        else if ("error".equalsIgnoreCase(level)) {
            loggingConfigService.setLoggingLevel(loggerName, LogLevel.ERROR);
        }
        else {
            loggingConfigService.setLoggingLevel(loggerName, LogLevel.INFO);
        }

        return getLoggingLevel(loggerName);
    }

    @EndpointOrder(value0 = 600, value1 = 2200)
    @GetMapping("/level/{loggerName}")
    public ResponseEntity<?> getLoggingLevel(@PathVariable(name = "loggerName") String loggerName) {
        LoggingLevelDomain response = new LoggingLevelDomain();
        response.setLoggerName(loggerName);
        response.setLevel(loggingConfigService.getLoggingLevel(loggerName).toString());

        ReturnCode code = ReturnCode.OK;
        return buildResponseEntity(code, response);
    }

    @EndpointOrder(value0 = 600, value1 = 3100)
    @PostMapping("/levels")
    public ResponseEntity<?> setAllLoggingLevels(@RequestBody List<LoggingLevelDomain> levels) {
        if (levels.size() == 0) {
            throw new BusinessException(ReturnCode.EMPTY_ARRAY, "Input array is empty");
        }
        for (LoggingLevelDomain item : levels) {
            loggingConfigService.setLoggingLevel(item.getLoggerName(), LogLevel.valueOf(item.getLevel()));
        }

        return getAllLoggingLevels();
    }

    @EndpointOrder(value0 = 600, value1 = 3200)
    @GetMapping("/levels")
    public ResponseEntity<?> getAllLoggingLevels() {
        List<LoggingLevelDomain> response = new ArrayList<>();
        for (String loggerName : loggerNames) {
            LoggingLevelDomain item = new LoggingLevelDomain();
            item.setLoggerName(loggerName);
            item.setLevel(loggingConfigService.getLoggingLevel(loggerName).toString());
            response.add(item);
        }

        ReturnCode code = ReturnCode.OK;
        return buildResponseEntity(code, response);
    }

    private ResponseEntity<?> buildResponseEntity(ReturnCode code, Object response) {
        GlobalResponse<?> result = new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(result);
    }
}
