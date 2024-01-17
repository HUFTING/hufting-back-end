package com.likelion.hufsting.domain.oauth.security.filter;


import com.nimbusds.jose.shaded.gson.Gson;
import com.likelion.hufsting.domain.oauth.security.exception.RefreshTokenException;
import com.likelion.hufsting.domain.oauth.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;


@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    // OncePerRerquestFilter는 하나의 요청에 대해서 한번씩 동작하는 필터

    private final String refreshPath;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response , FilterChain filterChain) throws ServletException , IOException {

        String path = request.getRequestURI();

        if(!path.equals(refreshPath)) {
            filterChain.doFilter(request, response);
            return;
        }


        //전송된 JSON에서 accessToken과 RefreshToken을 가져오기
        Map<String ,String> tokens = parseRequestJson(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");


        try{
            checkAccessToken(accessToken);
        }catch(RefreshTokenException refreshTokenException){
            //유효한 토큰이 아니라면 접근 제한
            refreshTokenException.sendResponseError(response);
            return; //더이상 실행할 필요 없음
        }

        Map<String ,Object> refreshClaims = null;

        try{
            refreshClaims = checkRefreshToken(refreshToken);

            //ReFresh Token의 유효 시간이 얼마 남지 않은 경우
            Integer exp = (Integer)refreshClaims.get("exp");

            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

            Date current =new Date(System.currentTimeMillis());

            //만료 시간과 현재 시간의 간격 계산
            //만일 3일 미만의 경우에는 Refresh Token도 다시 생성
            long gapTime = (expTime.getTime() - current.getTime());


            String mid = (String)refreshClaims.get("mid");

            // 여기까지 도달하면 무조건 AccessToken은 새로 생성
            String accessTokenValue = jwtUtil.generateToken(Map.of("mid",mid),1);
            String refreshTokenValue = tokens.get("refreshToken");

            //RefreshToken이 만료 기한이 3일미만이라면..
            if(gapTime < (1000* 60 * 60 *24 * 3)) {
                refreshTokenValue = jwtUtil.generateToken(Map.of("mid",mid), 30);
            }

            //새로운 토큰을 보내주는 로직
            sendTokens(accessTokenValue,refreshTokenValue,response);

        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return; //더 이상 실행할 코드 없음
        }

    }

    // request로 들어오는 JSON 데이터를 전처리하자
    private Map<String ,String > parseRequestJson(HttpServletRequest request) {

        //JSON 데이터를 분석해서 mid, mpw 전달 값을 Map으로 처리 -> 왜? ->
        try(Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try{
            jwtUtil.validateToken(accessToken);
        }catch (ExpiredJwtException expiredJwtException){
        }catch (Exception exception){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String , Object> checkRefreshToken(String refreshToken) throws RefreshTokenException{

        try{
            Map<String ,Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        }catch (ExpiredJwtException expiredJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch (MalformedJwtException malformedJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }catch (Exception exception){
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    // 재발급하는 토근이있는 경우
    private void sendTokens(String accessTokenvalue, String refreshTokenValue,HttpServletResponse response) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String  jsonStr = gson.toJson(Map.of("accessToken",accessTokenvalue,"refreshToken",refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }
}
