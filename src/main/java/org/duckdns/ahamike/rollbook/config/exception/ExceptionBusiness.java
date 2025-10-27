package org.duckdns.ahamike.rollbook.config.exception;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;

public class ExceptionBusiness extends RuntimeException {
    private final ReturnCode code;

    public ExceptionBusiness(ReturnCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ExceptionBusiness(ReturnCode code, String detail) {
        super(detail);
        this.code = code;
    }

    public ReturnCode getErrorCode() {
        return code;
    }
}
