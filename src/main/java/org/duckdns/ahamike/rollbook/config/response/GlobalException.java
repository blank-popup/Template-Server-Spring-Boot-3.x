package org.duckdns.ahamike.rollbook.config.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException {
    private String code;
    private String message;
    private String detail;
    @JsonIgnore
    private HttpStatus httpStatus;
    private int httpStatusValue;
    private String httpStatusPhrase;
    private String exceptionName;
    private String method;
    private String path;

    public GlobalException(String code, String message, String detail, HttpStatus httpStatus, String exceptionName, String method, String path) {
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.httpStatus = httpStatus;
        this.httpStatusValue = httpStatus.value();
        this.httpStatusPhrase = httpStatus.getReasonPhrase();
        this.exceptionName = exceptionName;
        this.method = method;
        this.path = path;
    }
}
