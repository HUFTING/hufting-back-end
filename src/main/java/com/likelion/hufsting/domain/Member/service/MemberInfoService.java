package com.likelion.hufsting.domain.Member.service;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.dto.MemberDetailInfoResponse;
import com.likelion.hufsting.domain.Member.dto.MemberInfoResponse;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.likelion.hufsting.domain.Member.repository.query.MemberQueryRepository;
import com.likelion.hufsting.domain.Member.validation.GlobalAuthMethodValidator;
import com.likelion.hufsting.domain.follow.domain.Follow;
import com.likelion.hufsting.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberInfoService {
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final FollowRepository followRepository;
    // validators
    private final GlobalAuthMethodValidator globalAuthMethodValidator;

    public MemberInfoResponse findByEmail(String email) {
        final String finalEmail;
        if(!email.contains("@hufs.ac.kr")) {
            finalEmail = email + "@hufs.ac.kr";
        } else {
            finalEmail = email;
        }
        Member member = memberRepository.findByEmail(finalEmail)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + finalEmail));
        return new MemberInfoResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhotoUrl(),
                member.getProfile().getContent()
        );
    }

    public List<MemberInfoResponse> getFollowerList(Authentication authentication) {
        System.out.println("authentication-Email 값" + authentication.getName());
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        return member.getFollowerList().stream()
                .map(follow -> new MemberInfoResponse(
                        follow.getFollowee().getId(),
                        follow.getFollowee().getName(),
                        follow.getFollowee().getEmail(),
                        follow.getFollowee().getPhotoUrl(),
                        follow.getFollowee().getProfile().getContent()))
                .collect(Collectors.toList());
    }

    public MemberDetailInfoResponse findMemberDetailInfoById(Long memberId, Authentication authentication){
        // get login user
        Member loginUser = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // validation-0 : 내가 팔로우하는 사람인지 확인
        List<Follow> followees = followRepository.findByFollower(loginUser);
        globalAuthMethodValidator.isMemberInFollwees(followees, memberId);
        // Get profile the member
        Member findMember = memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + memberId));
        return MemberDetailInfoResponse.builder()
                .id(findMember.getId())
                .name(findMember.getName())
                .major(findMember.getMajor())
                .studentNumber(findMember.getProfile().getStudentNumber())
                .age(findMember.getProfile().getBirthday())
                .mbti(findMember.getProfile().getMbti())
                .content(findMember.getProfile().getContent())
                .build();
    }
}
