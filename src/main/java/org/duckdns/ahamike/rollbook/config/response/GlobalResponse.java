package org.duckdns.ahamike.rollbook.config.response;

import java.util.Collection;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse<T> {
    private String code;
    private String message;
    private String detail;
    @JsonIgnore
    private HttpStatus httpStatus;
    private int httpStatusValue;
    private String httpStatusPhrase;
    private Long dataCount;
    private T data;

    public GlobalResponse(String code, String message, String detail, HttpStatus httpStatus, T data) {
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.httpStatus = httpStatus;
        this.httpStatusValue = httpStatus.value();
        this.httpStatusPhrase = httpStatus.getReasonPhrase();
        this.dataCount = calculateDataCount(data);
        this.data = data;
    }

    public GlobalResponse(String code, String message, HttpStatus httpStatus, T data) {
        this(code, message, null, httpStatus, data);
    }

    public GlobalResponse(ReturnCode returnCode, T data) {
        this(returnCode.getCode(), returnCode.getMessage(), null, returnCode.getHttpStatus(), data);
    }

    private Long calculateDataCount(T data) {
        if (data == null) {
            return 0L;
        } else if (data instanceof Collection<?>) {
            return Long.valueOf(((Collection<?>) data).size());
        } else if (data instanceof String) {
            return Long.valueOf(((String) data).length());
        } else {
            return 1L;
        }
    }
}
