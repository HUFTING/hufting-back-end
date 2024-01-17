package com.likelion.hufsting.domain.oauth.security.handler;


import com.nimbusds.jose.shaded.gson.Gson;
import com.likelion.hufsting.domain.oauth.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //응답은 JSON형태

        Map<String , Object> claim = Map.of("mid",authentication.getName());
        //Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);

        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim,30);

        Gson gson = new Gson();

        Map<String ,Object> keyMap = Map.of(
                "accessToken" , accessToken,
                "refreshToken" , refreshToken);

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);

    }
}
