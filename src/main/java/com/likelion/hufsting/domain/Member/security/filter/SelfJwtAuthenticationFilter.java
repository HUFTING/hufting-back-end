package com.likelion.hufsting.domain.Member.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.exception.InputNotFoundException;
import com.likelion.hufsting.domain.Member.security.SelfMemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class SelfJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final String INPUT_ERR_MSG = "입력된 요청이 유효하지 않습니다.";
    public SelfJwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Member member = new ObjectMapper().readValue(request.getInputStream(), Member.class);
            final UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        }catch (IOException e){
            throw new InputNotFoundException(INPUT_ERR_MSG);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SelfMemberDetails memberDetails = (SelfMemberDetails) authResult.getPrincipal();
        response.addHeader("Authorization", "Bearer " + "temporary token");
    }
}
