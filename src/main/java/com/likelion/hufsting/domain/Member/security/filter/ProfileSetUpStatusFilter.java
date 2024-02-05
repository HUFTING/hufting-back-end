package com.likelion.hufsting.domain.Member.security.filter;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.exception.ProfileSetUpStatusException;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.Member.security.GoogleOauthMemberDetails;
import com.likelion.hufsting.domain.Member.util.GoogleOauthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class ProfileSetUpStatusFilter extends OncePerRequestFilter {
    // constant
    private final String PROFILE_SET_UP_STATUS_ERR_MSG = "프로필이 설정되지 않았습니다.";
    // DI
    private final MemberRepository memberRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isRequireFiltering(request)){
            GoogleOauthMemberDetails googleOauthMemberDetails = (GoogleOauthMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = googleOauthMemberDetails.getName();
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow();
            if(member.getProfileSetUpStatus().equals(Boolean.FALSE)){
                throw new ProfileSetUpStatusException(PROFILE_SET_UP_STATUS_ERR_MSG);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Boolean isRequireFiltering(HttpServletRequest request){
        return isAuthenticated() && isNotRegisterProfileRequest(request);
    }

    // authentication 객체 여부 확인
    private Boolean isAuthenticated(){
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            return false;
        }
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    // profile 설정 요청이 아닌지 확인
    private Boolean isNotRegisterProfileRequest(HttpServletRequest request){
        // get http information
        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        // validation
        return !(requestMethod.equals("POST") && requestUri.equals("/api/v1/profile"));
    }
}
