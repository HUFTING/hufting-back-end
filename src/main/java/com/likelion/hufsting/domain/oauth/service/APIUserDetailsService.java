package com.likelion.hufsting.domain.oauth.service;


import com.likelion.hufsting.domain.oauth.domain.Member;
import com.likelion.hufsting.domain.oauth.dto.APIUserDTO;
import com.likelion.hufsting.domain.oauth.repository.APIUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    //주입
    private final APIUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> result = apiUserRepository.findByEmail(username);

        Member member = result.orElseThrow(() -> new UsernameNotFoundException("Cannot find mid")); // username에 해당하는 사용자가 있으면 유저 정보 반환 아니면 예외처리


        APIUserDTO dto = new APIUserDTO(
                member.getEmail(),
                member.getPw(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));


        return dto;
    }
}
