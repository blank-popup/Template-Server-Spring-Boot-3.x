package org.duckdns.ahamike.rollbook.config.logging.setting;

import java.io.IOException;

import org.duckdns.ahamike.rollbook.config.context.SpringContext;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestCachingFilter implements Filter {

    private final String requestParamInfoName = SpringContext.getRequestParamInfoName();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        log.debug("$$$ Enter RequestCachingFilter.doFilter");

        HttpServletRequest request = (HttpServletRequest) req;
        ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);

        RequestParamInfo info = new RequestParamInfo();
        request.setAttribute(requestParamInfoName, info);

        chain.doFilter(cachingRequest, res);
    }
}
