package org.duckdns.ahamike.rollbook.config.logging;

import java.io.IOException;

import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class RequestCachingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);

        InfoRequestParam info = new InfoRequestParam();
        request.setAttribute("InfoRequestParam", info);

        chain.doFilter(cachingRequest, res);
    }
}
