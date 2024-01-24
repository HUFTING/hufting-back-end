package com.likelion.hufsting.domain.oauth.controller;


import com.likelion.hufsting.domain.oauth.domain.Member;

import com.likelion.hufsting.domain.oauth.dto.APIAuthCodeDTO;
import com.likelion.hufsting.domain.oauth.repository.APIUserRepository;
import com.likelion.hufsting.domain.oauth.service.APILoginService;
import com.likelion.hufsting.domain.oauth.util.JWTUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

// https://accounts.google.com/o/oauth2/auth?client_id=386676289031-rh4v08860ffopeip90l4ss5en08ot4d6.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth2/code/google&response_type=code&scope=email%20profile

@RestController
@Log4j2
@RequestMapping(value = "/auth",produces = "application/json")
public class allowURIController {
    @Autowired
    private final JWTUtil jwtUtil = new JWTUtil();
    @Autowired
    private APIUserRepository apiUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    APILoginService apiLoginService;

    public allowURIController(APILoginService apiLoginService) {
        this.apiLoginService = apiLoginService;
    }

    @PostMapping("/code/{registrationId}/callback")
    public ResponseEntity<String> googleLogin(@RequestBody APIAuthCodeDTO code, @PathVariable String registrationId) {
        System.out.println(code.getCode());
        String deCode = URLDecoder.decode(code.getCode() ,StandardCharsets.UTF_8);
        Map<String ,String> userInfo;
        // code값으 DTO를 거치지 않으면 JSON형태의 문자열로 들어오게 된다.
        userInfo = apiLoginService.socialLogin(deCode, registrationId);
//        System.out.println("code 출력" + code.getCode());
//        System.out.println("================");
        // 회원가입된 소셜계정인지 확인
        String id = userInfo.get("email");
        String[] s = id.split("@");
        System.out.println(s[1]);
        if(!s[1].equals("hufs.ac.kr")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("학교 이메일 hufs.ac.kr 이메일로 로그인해주세요");
        }
        Optional<Member> user = apiUserRepository.findById(id);

        if (!user.isPresent()) { //유저가 존재하는 않는 경우
            String rawPassword = "비밀번호";
            String encodePassword = passwordEncoder.encode(rawPassword);
            Member member = new Member(id,encodePassword);
            apiUserRepository.save(member);
        }
        String accessToken = jwtUtil.generateToken(Map.of("mid", id), 1);
        String refreshToken = jwtUtil.generateToken(Map.of("mid", id),30);

        Gson gson = new Gson();

        Map<String ,Object> keyMap = Map.of(
                "accessToken" , accessToken,
                "refreshToken" , refreshToken,
                "name", userInfo.get("nickname"),
                "profile", userInfo.get("profile"));


        String jsonStr = gson.toJson(keyMap);
        System.out.println(jsonStr);

        return ResponseEntity.ok(jsonStr);
    }


}



