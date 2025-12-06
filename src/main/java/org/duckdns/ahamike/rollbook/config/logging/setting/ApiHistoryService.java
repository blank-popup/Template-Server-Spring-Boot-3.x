package org.duckdns.ahamike.rollbook.config.logging.setting;

import java.time.LocalDateTime;

import org.duckdns.ahamike.rollbook.table.ApiHistoryEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApiHistoryService {

    private final ApiHistoryRepository apiHistoryRepository;

    @Async
    public void record(String username, String method, String uri, String pathVariable, String requestParam, String requestPartFile, String requestPartParam, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt, Long duration, String requestBody, String responseBody) {
        ApiHistoryEntity history = new ApiHistoryEntity(username, method, uri, pathVariable, requestParam, requestPartFile, requestPartParam, ip, userAgent, httpStatusValue, createdAt, duration, requestBody, responseBody);
        apiHistoryRepository.save(history);
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
