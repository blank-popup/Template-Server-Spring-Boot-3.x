package org.duckdns.ahamike.rollbook.config.logging;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ServiceLoggingConfig {

    private final LoggingSystem loggingSystem;

    public void setLogLevel(String loggerName, LogLevel level) {
        loggingSystem.setLogLevel(loggerName, level);
    }

    public LogLevel getLogLevel(String loggerName) {
        return loggingSystem.getLoggerConfiguration(loggerName).getEffectiveLevel();
    }

    private final AtomicBoolean isLoggingRequest = new AtomicBoolean(true);
    private final AtomicBoolean isLoggingResponse = new AtomicBoolean(true);
    private final AtomicBoolean isLoggingException = new AtomicBoolean(true);
    private final AtomicBoolean isLoggingDatabase = new AtomicBoolean(true);

    public void enableRequest() {
        isLoggingRequest.set(true);
    }

    public void disableRequest() {
        isLoggingRequest.set(false);
    }

    public boolean isEnabledRequest() {
        return isLoggingRequest.get();
    }

    public void enableResponse() {
        isLoggingResponse.set(true);
    }

    public void disableResponse() {
        isLoggingResponse.set(false);
    }

    public boolean isEnabledResponse() {
        return isLoggingResponse.get();
    }

    public void enableException() {
        isLoggingException.set(true);
    }

    public void disableException() {
        isLoggingException.set(false);
    }

    public boolean isEnabledException() {
        return isLoggingException.get();
    }

    public void enableDatabase() {
        isLoggingDatabase.set(true);
    }

    public void disableDatabase() {
        isLoggingDatabase.set(false);
    }

    public boolean isEnabledDatabase() {
        return isLoggingDatabase.get();
    }
}
