package com.library.spring.security.filter;

import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class OAuth2AuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if(requestURI != null && requestURI.toLowerCase().startsWith("/web/secure")) {

            Cookie accessTokenCookie = WebUtils.getCookie(httpRequest, "access_token");

            if(accessTokenCookie != null) {
                String access_token = accessTokenCookie.getValue();

                MutableHttpRequestWrapper mutableRequest = new MutableHttpRequestWrapper(httpRequest);
                mutableRequest.addHeader("Authorization", "Bearer " + access_token);
                request = mutableRequest;
            }
        }

        chain.doFilter(request, response);
    }
}