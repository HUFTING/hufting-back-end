package com.likelion.hufsting.domain.oauth.security.filter;

import com.likelion.hufsting.domain.oauth.security.exception.AccessTokenException;
import com.likelion.hufsting.domain.oauth.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    // OncePerRerquestFilter는 하나의 요청에 대해서 한번씩 동작하는 필터

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException , IOException {

        String path = request.getRequestURI();
        // "/api/"로 시작하는 모든 경로의 호출에 사용
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            System.out.println("여기 들어옴 1 !!!!!!!!!!!!!!!!!!!!!");
            return;
        }


        try{
            System.out.println("호출 2!!!!!!!!!!!!");
            validateAccessToken(request);
            System.out.println("호출 3!!!!!!!!!!!!");
            filterChain.doFilter(request,response);
            System.out.println("호출 4!!!!!!!!!!");
        }catch (AccessTokenException accessTokenException){
            System.out.println(accessTokenException.getMessage());
            filterChain.doFilter(request,response); // 임시 테스트
            //accessTokenException.sendResponseError(response);
        }
    }

    private Map<String ,Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

        //request의 헤더에서 Authorization 부분 문자열을 추출
        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length() < 8) {
            //토큰을 제대로 받지 못한 경우
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        //Bearer 생략
        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);
        //tokenType  문자열을 대소문자 무시하고 비교한다 false로 되어있으니 Bearer와 다르면 예외발생.
        if(tokenType.equalsIgnoreCase("Bearer") == false) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String ,Object> values = jwtUtil.validateToken(tokenStr);

            return values;
        }catch(MalformedJwtException malformedJwtException){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
}
