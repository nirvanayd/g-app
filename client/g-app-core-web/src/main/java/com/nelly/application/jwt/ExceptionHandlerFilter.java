package com.nelly.application.jwt;

import com.nelly.application.dto.Response;
import com.nelly.application.exception.ExpireTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (ExpireTokenException ex){
            setErrorResponse(HttpStatus.LENGTH_REQUIRED, response, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ExpireTokenException ex){
        response.setStatus(status.value());
        response.setContentType("application/json");
        Response errorResponse = new Response();

        try{
            String json = errorResponse.convertToJson(null,
                    ex.getExceptionCode().getCode(), ex.getExceptionCode().getMessage());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
