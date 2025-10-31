package org.duckdns.ahamike.rollbook.config.logging;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.duckdns.ahamike.rollbook.config.logging.runtime.ServiceLoggingConfig;
import org.duckdns.ahamike.rollbook.util.client.ClientInfo;
import org.duckdns.ahamike.rollbook.util.json.JsonUtil;
import org.duckdns.ahamike.rollbook.util.str.Str;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {
    public static LogParameter buildLogParameter(ServiceApiHistory serviceApiHistory, ServiceLoggingConfig serviceLoggingConfig, ObjectMapper mapper, String uriSignUp, String uriSignIn, int maxRequestBodySize, int maxResponseBodySize) { 
        LogParameter logParameter = new LogParameter(
            serviceApiHistory, serviceLoggingConfig, mapper,
            uriSignUp, uriSignIn,
            maxRequestBodySize, maxResponseBodySize
        );

        return logParameter;
    }
    
    public static LogParameter setLogPreParameter(LogParameter logParameter, HttpServletRequest request, InfoRequestParam param) {
        String method = request.getMethod();
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String uriWithoutContextPath = uri.substring(contextPath.length());
        String query = request.getQueryString();
        String uriWithoutContextPathWithQuery = (query == null) ? uriWithoutContextPath : (uriWithoutContextPath + "?" + query);
        logParameter.setMethod(method);
        logParameter.setUri(uriWithoutContextPathWithQuery);

        logParameter.setPathVariable(param.getPathVariables().toString());
        logParameter.setRequestParam(Str.map2String(param.getRequestParams()));
        logParameter.setRequestPartFile(param.getRequestPartsFile().toString());
        logParameter.setRequestPartParam(Str.map2String(param.getRequestPartsParam()));

        logParameter.setRequestObjectBody(param.getRequestBody());

        String username = resolveUsername(logParameter);
        logParameter.setUsername(username);

        String ip = ClientInfo.getRemoteIP(request);
        logParameter.setIp(ip);
        String userAgent = request.getHeader("User-Agent");
        logParameter.setUserAgent(userAgent);
        String requestBody = getRequestBody(logParameter);
        logParameter.setRequestBody(requestBody);
    
        return logParameter;
    }

    public static LogParameter setLogPostParameter(LogParameter logParameter, HttpServletRequest request, Object response, Integer httpStatusValue) {
        logParameter.setHttpStatusValue(httpStatusValue);
        logParameter.setResponseObjectBody(response);

        String responseBody = getResponseBody(logParameter);
        logParameter.setResponseBody(responseBody);

        LocalDateTime createdAt = LocalDateTime.now();
        logParameter.setCreatedAt(createdAt);

        Long duration = getDuration(request, createdAt);
        logParameter.setDuration(duration);

        return logParameter;
    }

    public static String resolveUsername(LogParameter logParameter) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() == true && "anonymousUser".equals(auth.getPrincipal()) == false) {
            return auth.getName();
        }
        else if (logParameter.getMethod() != null && logParameter.getMethod().equals(HttpMethod.POST.name()) == true && logParameter.getUri() != null && (logParameter.getUri().equalsIgnoreCase(logParameter.getUriSignUp()) || logParameter.getUri().equalsIgnoreCase(logParameter.getUriSignIn())) && logParameter.getMapper() != null && logParameter.getRequestObjectBody() != null) {
            JsonNode body = JsonUtil.object2JsonNode(logParameter.getMapper(), logParameter.getRequestObjectBody());
            if (body != null && body.has("username")) {
                return body.get("username").asText();
            }
        }

        return null;
    }

    public static String getRequestBody(LogParameter logParameter) {
        String requestBody = null;

        JsonNode requestJsonBody = JsonUtil.object2JsonNode(logParameter.getMapper(), logParameter.getRequestObjectBody());
        if (requestJsonBody != null) {
            JsonUtil.sanitize(requestJsonBody, "password", "********");
            String jsonString = JsonUtil.object2JsonString(logParameter.getMapper(), requestJsonBody);
            requestBody = Str.truncate(jsonString, logParameter.getMaxRequestBodySize());
        } else {
            String jsonString = logParameter.getRequestObjectBody() == null ? null : String.valueOf(logParameter.getRequestObjectBody());
            requestBody = Str.truncate(jsonString, logParameter.getMaxRequestBodySize());
        }

        return requestBody;
    }

    public static Object getRequestObjectBody(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        Object body = null;

        for (int ii = 0; ii < parameterAnnotations.length; ++ii) {
            for (Annotation annotation : parameterAnnotations[ii]) {
                if (annotation.annotationType().equals(RequestBody.class) == true) {
                    body = args[ii];
                    break;
                }
            }
        }

        return body;
    }

    public static String getResponseBody(LogParameter logParameter) {
        String responseBody = null;

        JsonNode responseJsonBody = JsonUtil.object2JsonNode(logParameter.getMapper(), logParameter.getResponseObjectBody());
        if (responseJsonBody != null && responseJsonBody.has("body")) {
            responseJsonBody = responseJsonBody.get("body");
        }
        if (responseJsonBody != null) {
            JsonUtil.sanitize(responseJsonBody, "password", "********");
            String jsonString = JsonUtil.object2JsonString(logParameter.getMapper(), responseJsonBody);
            responseBody = Str.truncate(jsonString, logParameter.getMaxResponseBodySize());
        } else {
            String jsonString = logParameter.getResponseObjectBody() == null ? null : String.valueOf(logParameter.getResponseObjectBody()); 
            responseBody = Str.truncate(jsonString, logParameter.getMaxResponseBodySize());
        }

        return responseBody;
    }

    public static Long getDuration(HttpServletRequest request, LocalDateTime createdAt) {
        Long timeRequest = (Long) request.getAttribute("timeRequest");
        Long timeResponse = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long duration = (timeRequest != null) ? (timeResponse - timeRequest) : null;

        return duration;
    }

    public static String getCurrentFileName() {
        return Thread.currentThread().getStackTrace()[1].getFileName();
    }

    public static String getCurrentClassName() {
        return Thread.currentThread().getStackTrace()[1].getClassName();
    }

    public static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[1].getMethodName();
    }

    public static Integer getCurrentLineNumber() {
        return Thread.currentThread().getStackTrace()[1].getLineNumber();
    }
}
