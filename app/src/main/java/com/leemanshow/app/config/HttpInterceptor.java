package com.leemanshow.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("[REQUEST]: {} {} from {} with Headers {}", request.getMethod(), request.getRequestURI(), request.getRemoteHost() + ":" + request.getRemotePort(), getRequestHeaders(request));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("[RESPONSE]: {} from {}:{}, {} with Headers {}", request.getRequestURI(), request.getRemoteHost(), request.getRemotePort(), response.getStatus(), getResponseHeaders(response));
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private Map<String, String> getResponseHeaders(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }
}
