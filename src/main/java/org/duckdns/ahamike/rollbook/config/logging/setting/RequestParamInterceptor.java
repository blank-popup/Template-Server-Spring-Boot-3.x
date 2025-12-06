package org.duckdns.ahamike.rollbook.config.logging.setting;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.config.context.SpringContext;
import org.duckdns.ahamike.rollbook.config.logging.setting.RequestParamInfo.FileMeta;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestParamInterceptor implements HandlerInterceptor {

    private final String requestParamInfoName = SpringContext.getRequestParamInfoName();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.debug("$$$ Enter RequestParamInterceptor.preHandle");

        RequestParamInfo requestParamInfo = (RequestParamInfo) request.getAttribute(requestParamInfoName);
        if (requestParamInfo == null) {
            return true;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null) {
            requestParamInfo.setPathVariables(pathVariables);
        }

        if (request instanceof MultipartHttpServletRequest multipartRequest) {
            Map<String, MultipartFile> files = multipartRequest.getFileMap();
            List<FileMeta> requestPartsFile = extractFileMeta(files);
            requestParamInfo.setRequestPartsFile(requestPartsFile);

            Map<String, String[]> requestPartsParam = multipartRequest.getParameterMap();
            requestParamInfo.setRequestPartsParam(requestPartsParam);
        }
        else {
            Map<String, String[]> requestParams = request.getParameterMap();
            requestParamInfo.setRequestParams(requestParams);
        }

        return true;
    }

    private List<FileMeta> extractFileMeta(Map<String, MultipartFile> files) {
        return files.entrySet().stream()
                .map(entry -> {
                    MultipartFile file = entry.getValue();
                    return new FileMeta(
                            entry.getKey(),
                            file.getOriginalFilename(),
                            file.getContentType()
                    );
                })
                .collect(Collectors.toList());
    }
}
