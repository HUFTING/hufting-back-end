package com.likelion.hufsting.domain.Member.service;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.dto.MemberDetailInfoResponse;
import com.likelion.hufsting.domain.Member.dto.MemberInfoResponse;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.likelion.hufsting.domain.Member.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberInfoService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    public MemberInfoResponse findByEmail(String Email) {
        Member member = memberRepository.findByEmail(Email)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + Email));
        Long id = member.getId();
        String name = member.getName();
        String content = member.getProfile().getContent();

        return new MemberInfoResponse(id, name, content);

    }

    public List<MemberInfoResponse> getFollowerList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        return member.getFollowerList().stream()
                .map(follow -> new MemberInfoResponse(
                        follow.getFollowee().getId(),
                        follow.getFollowee().getName(),
                        follow.getFollowee().getProfile().getContent()))
                .collect(Collectors.toList());
    }

    public MemberDetailInfoResponse findMemberDetailInfoById(Long memberId){
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
