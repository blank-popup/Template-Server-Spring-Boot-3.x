package org.duckdns.ahamike.rollbook.config.logging.setting;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.duckdns.ahamike.rollbook.config.logging.ServiceLoggingConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Aspect
@Slf4j
public class LogAop {

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

    @Pointcut("execution(* org.duckdns.ahamike..Controller*.*(..))")
    public void controller() {}

    @Around("controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("RequestContextHolder.getRequestAttributes() returned null");
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        Object infoObject = request.getAttribute("InfoRequestParam");
        InfoRequestParam infoRequestParam = (InfoRequestParam) infoObject;

        LogParameter logParameter = LogUtil.buildLogParameter(
            serviceApiHistory, serviceLoggingConfig, mapper,
            uriSignUp, uriSignIn,
            maxRequestBodySize, maxResponseBodySize
        );
        Object requestObjectBody = LogUtil.getRequestObjectBody(joinPoint);
        infoRequestParam.setRequestBody(requestObjectBody);

        logParameter = LogUtil.setLogPreParameter(logParameter, request, infoRequestParam);

        if (serviceLoggingConfig.isEnabledRequest() == true) {
            log.info("\n[Request] username: {}"
                    + "\n[Request] URI: [{}] {}"
                    + "\n[Request] PathVariable: {}"
                    + "\n[Request] RequestParam: {}"
                    + "\n[Request] RequestPart File: {}"
                    + "\n[Request] RequestPart Param: {}"
                    + "\n[Request] RequestBody: {}"
                    + "\n[Request] IP: {}"
                    + "\n[Request] userAgent: {}",
                logParameter.getUsername(),
                logParameter.getMethod(),
                logParameter.getUri(),
                logParameter.getPathVariable(),
                logParameter.getRequestParam(),
                logParameter.getRequestPartFile(),
                logParameter.getRequestPartParam(),
                logParameter.getRequestBody(),
                logParameter.getIp(),
                logParameter.getUserAgent()
            );
        }

        try {
            Object result = joinPoint.proceed();

            logParameter = LogUtil.setLogPostParameter(logParameter, request, result, HttpServletResponse.SC_OK);

            if (serviceLoggingConfig.isEnabledDatabase() == true) {
                serviceApiHistory.record(
                    logParameter.getUsername(),
                    logParameter.getMethod(),
                    logParameter.getUri(),
                    logParameter.getPathVariable(),
                    logParameter.getRequestParam(),
                    logParameter.getRequestPartFile(),
                    logParameter.getRequestPartParam(),
                    logParameter.getIp(),
                    logParameter.getUserAgent(),
                    logParameter.getHttpStatusValue(),
                    logParameter.getCreatedAt(),
                    logParameter.getDuration(),
                    logParameter.getRequestBody(),
                    logParameter.getResponseBody()
                );
            }
            if (serviceLoggingConfig.isEnabledResponse() == true) {
                log.info("\n[Response] HttpStatusValue: {}"
                        + "\n[Response] Duration: {}"
                        + "\n[Response] ResponseBody: {}",
                    logParameter.getHttpStatusValue(),
                    logParameter.getDuration(),
                    logParameter.getResponseBody()
                );
            }

            return result;
        } catch (Exception e) {
            throw e;
        }
    }
}
