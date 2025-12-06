package org.duckdns.ahamike.rollbook.config.response;

public class BusinessException extends RuntimeException {

    private final ReturnCode code;

    public BusinessException(ReturnCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public BusinessException(ReturnCode code, String detail) {
        super(detail);
        this.code = code;
    }

    public ReturnCode getErrorCode() {
        return code;
    }
}
