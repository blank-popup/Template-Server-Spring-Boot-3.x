package org.duckdns.ahamike.rollbook.config.constant;

import org.springframework.http.HttpStatus;

public enum ReturnCode {

    OK("OK", "All is well", HttpStatus.OK),

    // Database
    FAIL_TO_INSERT("FAIL_TO_INSERT", "Failed to insert", HttpStatus.BAD_REQUEST),
    FAIL_TO_SELECT("FAIL_TO_SELECT", "Failed to select", HttpStatus.BAD_REQUEST),
    FAIL_TO_UPDATE("FAIL_TO_UPDATE", "Failed to update", HttpStatus.BAD_REQUEST),
    FAIL_TO_DELETE("FAIL_TO_DELETE", "Failed to delete", HttpStatus.BAD_REQUEST),
    // File
    FAIL_TO_OPEN_FILE("FAIL_TO_OPEN_FILE", "Failed to open file", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_TO_SAVE_FILE("FAIL_TO_SAVE_FILE", "Failed to save file", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_TO_READ_FILE("FAIL_TO_READ_FILE", "Failed to read file", HttpStatus.INTERNAL_SERVER_ERROR),
    // Redis
    FAIL_TO_SET("FAIL_TO_SET", "Fail to set", HttpStatus.BAD_REQUEST),
    FAIL_TO_GET("FAIL_TO_GET", "Fail to get", HttpStatus.BAD_REQUEST),
    FAIL_TO_REMOVE("FAIL_TO_REMOVE", "Fail to remove", HttpStatus.BAD_REQUEST),

    NO_SUCH_DATA("NO_SUCH_DATA", "No such data", HttpStatus.BAD_REQUEST),
    TOO_MANY_DATA("TOO_MANY_DATA", "Too many data", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_EXISTS("DATA_ALREADY_EXISTS", "Data already exists", HttpStatus.BAD_REQUEST),
    DATA_NOT_ASSIGNED("DATA_NOT_ASSIGNED", "Data not assigned", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_ASSIGNED("DATA_ALREADY_ASSIGNED", "Data already assigned", HttpStatus.BAD_REQUEST),
    EVERY_CONDITION_IS_NULL("EVERY_CONDITION_IS_NULL", "Every condition is null", HttpStatus.BAD_REQUEST),
    NO_ESSENTIAL_DATA("NO_ESSENTIAL_DATA", "No essential data", HttpStatus.BAD_REQUEST),
    NOT_NULL_AND_WHITESPACE("NOT_NULL_AND_WHITESPACE", "Not null and whitespace data", HttpStatus.BAD_REQUEST),
    EMPTY_ARRAY("EMPTY_ARRAY", "Empty array", HttpStatus.BAD_REQUEST),
    EMPTY_OBJECT("EMPTY_OBJECT", "Empty object", HttpStatus.BAD_REQUEST),
    NOT_AVAILABLE_VALUE("NOT_AVAILABLE_VALUE", "Not available value", HttpStatus.BAD_REQUEST),
    UNAVAILABLE_MIME_TYPE("UNAVAILABLE_MIME_TYPE", "Unavailable mime type", HttpStatus.BAD_REQUEST),
    FILE_IO_ERROR("FILE_IO_ERROR", "File input/output error", HttpStatus.BAD_REQUEST),

    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "Username not found", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_AUTHENTICATION("INSUFFICIENT_AUTHENTICATION", "Insufficient authentication", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("ACCESS_DENIED", "Access denied", HttpStatus.FORBIDDEN),
    DATA_INTEGRITY_VIOLATION("DATA_INTEGRITY_VIOLATION", "Data integrity violation", HttpStatus.BAD_REQUEST),
    NOT_SIGNED_IN("NOT_SIGNED_IN", "Not signed in", HttpStatus.BAD_REQUEST),
    // NoResourceFoundException - 404 Not Found
    NOT_FOUND("NOT_FOUND", "Not found", HttpStatus.NOT_FOUND),
    // HttpMessageNotReadableException - 400 Bad Request
    NO_BODY("NO_BODY", "No body", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "Illegal Argument", HttpStatus.BAD_REQUEST),
    NULL_POINTER("NULL_POINTER", "Null pointer", HttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_NOT_VALID("METHOD_ARGUMENT_NOT_VALID", "Method argument not valid", HttpStatus.BAD_REQUEST),
    INDEX_OUT_OF_BOUNDS("INDEX_OUT_OF_BOUNDS", "Index out of bounds", HttpStatus.BAD_REQUEST),
    NUMBER_FORMAT("NUMBER_FORMAT", "Number format", HttpStatus.BAD_REQUEST),
    CLASS_CAST("CLASS_CAST", "Class cast", HttpStatus.BAD_REQUEST),
    // Exception - 500 Internal Server Error
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    CODE_NULL(null, null, null);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ReturnCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getStatusValuePhrase() {
        if (httpStatus == null) {
            return null;
        }
        return Integer.toString(httpStatus.value()) + " " + httpStatus.getReasonPhrase();
    }
}
