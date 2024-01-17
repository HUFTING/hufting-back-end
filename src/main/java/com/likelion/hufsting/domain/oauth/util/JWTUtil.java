package com.likelion.hufsting.domain.oauth.util;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


@Component
@Log4j2

public class JWTUtil {

    @Value("${org.zerock.jwt.secret}")
    private String key;

    // JWT생성을 위한 로직
    public String generateToken(Map<String, Object> valueMap, int days) {
        log.info("generateKey..."  + key);

        //헤더 부분
        Map<String , Object> headers = new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");

        // payload 부분설정
        Map<String ,Object> payloads = new HashMap<>();
        payloads.putAll(valueMap); // Map형식으로 받은걸 모두 payloads에 넣는다.

        //테스트시에는 짧은 유효 기간
        int time = (60* 24) * days; // 테스트 분단위로 나중에 60  *24 (일) 단위변경

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).
                        toInstant()))
                .signWith(SignatureAlgorithm.HS256,key.getBytes())
                .compact();

        return jwtStr;
    }

    public Map<String,Object> validateToken(String token)throws JwtException {

        Map<String ,Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes()) // Set Key
                .parseClaimsJws(token) // 파싱 및 검증 , 실패시 에러
                .getBody();
        return claim;
    }
}
