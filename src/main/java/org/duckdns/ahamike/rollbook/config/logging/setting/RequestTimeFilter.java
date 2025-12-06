package org.duckdns.ahamike.rollbook.config.logging.setting;

import java.io.IOException;

import org.duckdns.ahamike.rollbook.config.context.SpringContext;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestTimeFilter implements Filter {

    private final String requestTimeName = SpringContext.getRequestTimeName();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.debug("$$$ Enter RequestTimeFilter.doFilter");

        // ContentCachingRequestWrapper requestWrapper = request instanceof ContentCachingRequestWrapper wrapper ? wrapper : new ContentCachingRequestWrapper((HttpServletRequest) request);
        // ContentCachingResponseWrapper responseWrapper = response instanceof ContentCachingResponseWrapper wrapper ? wrapper : new ContentCachingResponseWrapper((HttpServletResponse) response);
        // chain.doFilter(requestWrapper, response);
        Long timeRequest = System.currentTimeMillis();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpRequest.setAttribute(requestTimeName, timeRequest);

        chain.doFilter(request, response);
    }
}
