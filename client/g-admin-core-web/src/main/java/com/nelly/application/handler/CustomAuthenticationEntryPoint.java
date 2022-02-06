package com.nelly.application.handler;

import com.nelly.application.exception.enums.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");
        if (exception == null) {
            ExceptionCode exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
            setResponse(response, exceptionCode);
            return;
        }
        if (exception.equals("expiredToken")) {
            ExceptionCode exceptionCode = ExceptionCode.EXPIRED_TOKEN_EXCEPTION;
            setResponse(response, exceptionCode);
            return;
        }
    }

    private void setResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + exceptionCode.getMessage() + "\""
                + ", \"code\" : \"" +  exceptionCode.getCode() + "\""
                + ", \"errors\" : [ ] }");
    }
}
