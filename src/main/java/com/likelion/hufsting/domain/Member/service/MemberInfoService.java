package com.likelion.hufsting.domain.Member.service;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.dto.MemberDetailInfoResponse;
import com.likelion.hufsting.domain.Member.dto.MemberInfoResponse;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;

import com.likelion.hufsting.domain.follow.service.FollowService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public MemberInfoResponse findByEmail(String email, Authentication authentication) {
        final String finalEmail;
        if(!email.contains("@hufs.ac.kr")) {
            finalEmail = email + "@hufs.ac.kr";
        } else {
            finalEmail = email;
        }
//        Member member = memberRepository.findByEmail(finalEmail)
//                .orElseThrow(() -> new IllegalArgumentException("not found: " + finalEmail));
        Optional<Member> member = memberRepository.findByEmail(finalEmail);
        if(member.isPresent()) {
            boolean isFollowingResponse = isFollowing(authentication, member.get());
            return new MemberInfoResponse(
                    member.get().getId(),
                    member.get().getName(),
                    member.get().getEmail(),
                    member.get().getPhotoUrl(),
                    member.get().getProfile().getContent(),
                    isFollowingResponse);
        } else {
            return new MemberInfoResponse();
        }
    }

    public boolean isFollowing(Authentication authentication, Member findMember) {
        Member currentMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));; // 본인
        System.out.println(currentMember.getFollowerList());
        System.out.println("findMember id " + findMember.getId());
        System.out.println(currentMember.getFollowerList().toString());
        System.out.println("findMember id " + currentMember.getFolloweeList());


        return currentMember.getFollowerList().stream()
                .anyMatch(follow -> follow.getFollowee().getId().equals(findMember.getId()));
    }

    public List<MemberInfoResponse> getFollowerList(Authentication authentication) {
        System.out.println("authentication-Email 값" + authentication.getName());
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        List<MemberInfoResponse> followerInfoList = new ArrayList<>();

        for (Follow follow : member.getFollowerList()) {
            Member followee = follow.getFollowee();
            boolean isFollowing = true;

            MemberInfoResponse memberInfoResponse = new MemberInfoResponse(
                    followee.getId(),
                    followee.getName(),
                    followee.getEmail(),
                    followee.getPhotoUrl(),
                    followee.getProfile().getContent(),
                    isFollowing  // 팔로우 여부 설정
            );
            followerInfoList.add(memberInfoResponse);
        }

        return followerInfoList;
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
                .gender(findMember.getProfile().getGender())
                .studentNumber(findMember.getProfile().getStudentNumber())
                .age(findMember.getProfile().getAge())
                .mbti(findMember.getProfile().getMbti())
                .content(findMember.getProfile().getContent())
                .build();
    }
}
