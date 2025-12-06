package org.duckdns.ahamike.rollbook.table;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_api_history")
@AllArgsConstructor
@NoArgsConstructor
public class ApiHistoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "method")
    private String method;

    @Column(name = "uri")
    private String uri;

    @Column(name = "path_variable", columnDefinition = "TEXT")
    private String pathVariable;

    @Column(name = "request_param", columnDefinition = "TEXT")
    private String requestParam;

    @Column(name = "request_part_file", columnDefinition = "TEXT")
    private String requestPartFile;

    @Column(name = "request_part_param", columnDefinition = "TEXT")
    private String requestPartParam;

    @Column(name = "ip")
    private String ip;

    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "http_status_value")
    private Integer httpStatusValue;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "request_body", columnDefinition = "LONGTEXT")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "LONGTEXT")
    private String responseBody;

    public ApiHistoryEntity(String username, String method, String uri, String pathVariable, String requestParam, String requestPartFile, String requestPartParam, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt, Long duration, String requestBody, String responseBody) {
        this.username = username;
        this.method = method;
        this.uri = uri;

        this.pathVariable = pathVariable;
        this.requestParam = requestParam;
        this.requestPartFile = requestPartFile;
        this.requestPartParam = requestPartParam;

        this.ip = ip;
        this.userAgent = userAgent;

        this.httpStatusValue = httpStatusValue;
        this.createdAt = createdAt;
        this.duration = duration;

        this.requestBody = requestBody;
        this.responseBody = responseBody;
    }

    public ApiHistoryEntity(String username, String method, String uri, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt, Long duration, String requestBody, String responseBody) {
        this(username, method, uri, null, null, null, null, ip, userAgent, httpStatusValue, createdAt, duration, requestBody, responseBody);
    }

    public ApiHistoryEntity(String username, String method, String uri, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt, Long duration) {
        this(username, method, uri, null, null, null, null, ip, userAgent, httpStatusValue, createdAt, duration, null, null);
    }

    public ApiHistoryEntity(String username, String method, String uri, String ip, String userAgent, Integer httpStatusValue, LocalDateTime createdAt) {
        this(username, method, uri, null, null, null, null, ip, userAgent, httpStatusValue, createdAt, null, null, null);
    }
}
