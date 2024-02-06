package com.likelion.hufsting.domain.Member.service;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.dto.MemberInfoResponse;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberInfoService {

    private final MemberRepository memberRepository;
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
}
