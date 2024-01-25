package com.likelion.hufsting.domain.oauth.service;


import com.likelion.hufsting.domain.oauth.domain.Member;
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

        return apiUserRepository.save(Member.builder()
                .email(dto.getMEmail())
                .pw(encoder.encode(dto.getMpw()))
                .build()).getEmail();
    }

    public Member findByMid(String email) {
        return apiUserRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
