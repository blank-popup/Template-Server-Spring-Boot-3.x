package org.duckdns.ahamike.rollbook.config.exception;

import java.io.IOException;
import java.lang.reflect.Type;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.logging.LogParameter;
import org.duckdns.ahamike.rollbook.config.logging.LogUtil;
import org.duckdns.ahamike.rollbook.config.logging.ServiceApiHistory;
import org.duckdns.ahamike.rollbook.config.logging.setting.ServiceLoggingConfig;
import org.duckdns.ahamike.rollbook.process.GlobalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements RequestBodyAdvice{
    private static final ThreadLocal<Object> HOLDER_BODY = new ThreadLocal<>();

    private final ServiceApiHistory serviceApiHistory;
    private final ObjectMapper mapper;
    private final ServiceLoggingConfig serviceLoggingConfig;

    @Value("${auth.permitAll.signUp}")
    private String uriSignUp;
    @Value("${auth.permitAll.signIn}")
    private String uriSignIn;
    @Value("${logging.limit.maxRequestBodySize}")
    private int maxRequestBodySize;
    @Value("${logging.limit.maxResponseBodySize}")
    private int maxResponseBodySize;

    @ExceptionHandler(ExceptionBusiness.class)
    public ResponseEntity<?> handleExceptionBusiness(ExceptionBusiness ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ex.getErrorCode();
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.NO_BODY;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.NOT_FOUND;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.USERNAME_NOT_FOUND;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.INSUFFICIENT_AUTHENTICATION;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.ACCESS_DENIED;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.ILLEGAL_ARGUMENT;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.NULL_POINTER;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.METHOD_ARGUMENT_NOT_VALID;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }
    
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<?> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.INDEX_OUT_OF_BOUNDS;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }
    
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleNumberFormatException(NumberFormatException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.NUMBER_DORMAT;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<?> handleClassCastException(ClassCastException ex, HttpServletRequest httpServletRequest) {
        ReturnCode code = ReturnCode.CLASS_CAST;
        ResponseEntity<?> response = buildResponseEntity(code, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, HttpServletRequest httpServletRequest) {
        log.error("Enter General Exception Handler");
        // throw ex;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof ResponseStatusException rse) {
            httpStatus = (HttpStatus) rse.getStatusCode();
        } else if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
            ResponseStatus annotation = ex.getClass().getAnnotation(ResponseStatus.class);
            httpStatus = annotation.value();
        }

        ResponseEntity<?> response = buildResponseEntity(httpStatus, ex, httpServletRequest);
        logHistory(response);
        return response;
    }

    private ResponseEntity<?> buildResponseEntity(ReturnCode code, Exception ex, HttpServletRequest httpServletRequest) {
        GlobalException response = new GlobalException(
                code.getCode(),
                code.getMessage(),
                ex.getMessage(),
                code.getHttpStatus(),
                ex.getClass().getName(),
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI()
        );
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(response);
    }

    private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, Exception ex, HttpServletRequest httpServletRequest) {
        GlobalException response = new GlobalException(
                "EXCEPTION",
                "General Exception",
                ex.getMessage(),
                httpStatus,
                ex.getClass().getName(),
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI()
        );
        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    private void logHistory(ResponseEntity<?> response) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("RequestContextHolder.getRequestAttributes() returned null");
            return;
        }

        Object requestObjectBody = HOLDER_BODY.get();
        LogParameter logParameter = LogUtil.buildLogParameter(
            serviceApiHistory, serviceLoggingConfig, mapper,
            uriSignUp, uriSignIn,
            maxRequestBodySize, maxResponseBodySize,
            requestObjectBody, null
        );

        HttpServletRequest request = attributes.getRequest();

        logParameter = LogUtil.setLogPreParameter(logParameter, request);
        Integer httpStatusValue = response.getStatusCode().value();
        logParameter = LogUtil.setLogPostParameter(logParameter, request, response, httpStatusValue);

        if (serviceLoggingConfig.isEnabledDatabase() == true) {
            serviceApiHistory.record(
                logParameter.getUsername(),
                logParameter.getMethod(),
                logParameter.getUri(),
                logParameter.getIp(),
                logParameter.getUserAgent(),
                logParameter.getHttpStatusValue(),
                logParameter.getCreatedAt(),
                logParameter.getDuration(),
                logParameter.getRequestBody(),
                logParameter.getResponseBody()
            );
        }
        if (serviceLoggingConfig.isEnabledRequest() == true) {
            log.debug("\n[Exception] username: {}\n[Exception] URI: [{}] {}\n[Exception] IP: {}\n[Exception] userAgent: {}\n[Exception] body: {}",
            logParameter.getUsername(), logParameter.getMethod(), logParameter.getUri(), logParameter.getIp(), logParameter.getUserAgent(), logParameter.getRequestBody());
        }
        if (serviceLoggingConfig.isEnabledException() == true) {
            log.debug("\n[Exception] httpStatusValue: {}\n[Exception] duration: {}\n[Exception] body: {}",
            logParameter.getHttpStatusValue(), logParameter.getDuration(), logParameter.getResponseBody());
        }
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        HOLDER_BODY.remove();
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        HOLDER_BODY.set(body);
        return body;
    }

    @Override
    public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
