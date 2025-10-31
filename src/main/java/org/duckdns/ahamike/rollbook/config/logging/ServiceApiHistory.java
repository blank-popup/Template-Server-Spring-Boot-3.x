package org.duckdns.ahamike.rollbook.config.logging;

import java.time.LocalDateTime;

import org.duckdns.ahamike.rollbook.table.EntityApiHistory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceApiHistory {

    private final RepositoryApiHistory repositoryApiHistory;

    @Async
    public void record(String username, String method, String uri, String pathVariable, String requestParam, String requestPartFile, String requestPartParam, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt, Long duration, String requestBody, String responseBody) {
        EntityApiHistory history = new EntityApiHistory(username, method, uri, pathVariable, requestParam, requestPartFile, requestPartParam, ip, userAgent, httpStatusValue, createdAt, duration, requestBody, responseBody);
        repositoryApiHistory.save(history);
    }

    @Async
    public void record(String username, String method, String uri, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt, Long duration, String requestBody, String responseBody) {
        record(username, method, uri, null, null, null, null, ip, userAgent, httpStatusValue, createdAt, duration, requestBody, responseBody);
    }

    @Async
    public void record(String username, String method, String uri, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt, Long duration) {
        record(username, method, uri, null, null, null, null, ip, userAgent, httpStatusValue, createdAt, duration, null, null);
    }

    @Async
    public void record(String username, String method, String uri, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt) {
        record(username, method, uri, null, null, null, null, ip, userAgent, httpStatusValue, createdAt, null, null, null);
    }
}
