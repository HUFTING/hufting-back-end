package com.likelion.hufsting.domain.Member.util;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.security.GoogleOauthMemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "hufsting"; // 변경 필요
    private final String COOKIE_ACCESS_TOKEN_KEY = "access_token";
    private final String JWT_CLAIM_SUBJECT = "hufsting";
    private final String JWT_CLAIM_EMAIL_KEY = "email";

    public String extractEmail(String token){
        Claims claims = extractAllClaims(token);
        return (String) claims.getOrDefault(JWT_CLAIM_EMAIL_KEY, "");

    }

    public Date extractExpiration(String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    // 토큰 생성 요청 메서드
    public String generateAccessToken(Member member){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", member.getEmail());
        return createToken(claims);
    }

    // 토큰 유효성 검사 요청 메서드
    public Boolean validateToken(String token){
        final String email = extractEmail(token);
        return (!email.isBlank() && !isTokenExpired(token));
    }

    // 토큰 생성 메서드
    private String createToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(JWT_CLAIM_SUBJECT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String getAccessToken(HttpServletRequest request){
        // 쿠키를 전송하지 않았을 경우
        if(request.getCookies() == null){
            return null;
        }
        // 쿠키를 받았을 경우
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals(COOKIE_ACCESS_TOKEN_KEY)){
                return cookie.getValue();
            }
        }
        return null;
    }

    // 토큰 만료 유효성 검사
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
    }
}
