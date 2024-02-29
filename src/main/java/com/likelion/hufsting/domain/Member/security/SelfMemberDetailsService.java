package com.likelion.hufsting.domain.Member.security;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelfMemberDetailsService implements UserDetailsService {
    private final String MEMBER_NOT_FOUND_ERR_MSG = "존재하지 않는 사용자입니다.";
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(MEMBER_NOT_FOUND_ERR_MSG));
        return new SelfMemberDetails(member);
    }
}
