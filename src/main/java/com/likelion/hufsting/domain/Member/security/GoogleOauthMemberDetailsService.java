package com.likelion.hufsting.domain.Member.security;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.domain.Role;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOauthMemberDetailsService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("여기 들어옴");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member;
        if(optionalMember.isEmpty()){
            member = Member.builder()
                    .email(email)
                    .role(Role.ROLE_USER)
                    .build();
            memberRepository.save(member);
        }else{
            member = optionalMember.get();
        }
        return new GoogleOauthMemberDetails(member, oAuth2User.getAttributes());
    }
}
