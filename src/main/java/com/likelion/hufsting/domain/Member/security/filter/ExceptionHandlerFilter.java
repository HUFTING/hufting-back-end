package com.likelion.hufsting.domain.Member.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.hufsting.domain.Member.dto.FilterErrorResponse;
import com.likelion.hufsting.domain.Member.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (JwtAuthenticationException e){
            ObjectMapper om = new ObjectMapper();
            FilterErrorResponse errorResponse = new FilterErrorResponse(e.getMessage());
            response.getWriter().write(om.writeValueAsString(errorResponse));
        }
    }
}
