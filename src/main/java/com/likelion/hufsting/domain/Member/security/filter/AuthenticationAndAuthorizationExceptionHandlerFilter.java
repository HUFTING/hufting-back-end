package com.likelion.hufsting.domain.Member.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.hufsting.domain.Member.dto.FilterErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class AuthenticationAndAuthorizationExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        try{
            filterChain.doFilter(request, response);
        } catch (Exception e){ // 나머지 에러
            FilterErrorResponse errorResponse = new FilterErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            String convertedErrorResponse = om.writeValueAsString(errorResponse);
            response.setContentType("application/json"); // 한글 깨짐 방지
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(convertedErrorResponse);
        }
    }
}
