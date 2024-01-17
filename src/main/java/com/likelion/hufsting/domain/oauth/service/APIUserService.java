package com.likelion.hufsting.domain.oauth.service;


import com.likelion.hufsting.domain.oauth.domain.APIUser;
import com.likelion.hufsting.domain.oauth.dto.APIAddUserDTO;
import com.likelion.hufsting.domain.oauth.repository.APIUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class APIUserService {

    private final APIUserRepository apiUserRepository;

    public String save(APIAddUserDTO dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return apiUserRepository.save(APIUser.builder()
                .mid(dto.getMid())
                .mpw(encoder.encode(dto.getMpw()))
                .build()).getMid();
    }

    public APIUser findByMid(String email) {
        return apiUserRepository.findByMid(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
