package com.example.application.views.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.Session;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

// TODO: https://www.baeldung.com/spring-security-basic-authentication
@Component
@Order(6)
public class MyAuhFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        StandardSessionFacade  session = (StandardSessionFacade ) req.getSession(true);
        // session.
        // session.setPrincipal(new UsernamePasswordAuthenticationToken("test", "test"));
        chain.doFilter(request, response);
    }

}
