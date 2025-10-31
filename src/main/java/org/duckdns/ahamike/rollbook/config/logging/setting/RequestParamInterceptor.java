package org.duckdns.ahamike.rollbook.config.logging.setting;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.config.logging.InfoRequestParam;
import org.duckdns.ahamike.rollbook.config.logging.InfoRequestParam.FileMeta;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        InfoRequestParam infoRequestParam = (InfoRequestParam) request.getAttribute("InfoRequestParam");
        if (infoRequestParam == null) {
            return true;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null) {
            infoRequestParam.setPathVariables(pathVariables);
        }

        if (request instanceof MultipartHttpServletRequest multipartRequest) {
            Map<String, MultipartFile> files = multipartRequest.getFileMap();
            List<FileMeta> requestPartsFile = extractFileMeta(files);
            infoRequestParam.setRequestPartsFile(requestPartsFile);

            Map<String, String[]> requestPartsParam = multipartRequest.getParameterMap();
            infoRequestParam.setRequestPartsParam(requestPartsParam);
        }
        else {
            Map<String, String[]> requestParams = request.getParameterMap();
            infoRequestParam.setRequestParams(requestParams);
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
