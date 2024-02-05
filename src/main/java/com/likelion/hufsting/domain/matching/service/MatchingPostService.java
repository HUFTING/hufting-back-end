package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.matchingpost.CreateMatchingPostData;
import com.likelion.hufsting.domain.matching.dto.matchingpost.FindMyMatchingPostData;
import com.likelion.hufsting.domain.matching.dto.matchingpost.FindMyMatchingPostResponse;
import com.likelion.hufsting.domain.matching.dto.matchingpost.UpdateMatchingPostData;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.matching.repository.query.MatchingPostQueryRepository;
import com.likelion.hufsting.domain.matching.validation.MatchingPostMethodValidator;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.validation.ProfileMethodValidator;
import com.likelion.hufsting.global.exception.AuthException;
import com.likelion.hufsting.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingPostService {
    // repositories
    private final MatchingPostRepository matchingPostRepository;
    private final MatchingPostQueryRepository matchingPostQueryRepository;
    private final MemberRepository memberRepository;
    // validators
    private final ProfileMethodValidator profileMethodValidator;
    private final MatchingPostMethodValidator matchingPostMethodValidator;
    // utils
    private final AuthUtil authUtil;

    // 훕팅 글 전체 조회
    public List<MatchingPost> findAllMatchingPost(){
        return matchingPostRepository.findAll();
    }
    // 훕팅 글 상세 조회
    public MatchingPost findByIdMatchingPost(Long matchingPostId){
        return matchingPostRepository.findById(matchingPostId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingPostId));
    }
    // 훕팅 글 등록
    @Transactional
    public Long saveMatchingPost(CreateMatchingPostData dto, Authentication authentication){
        // MatchingPost 생성
        Member author = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        MatchingPost matchingPost = new MatchingPost(
                dto.getTitle(),
                dto.getContent(),
                dto.getGender(),
                dto.getDesiredNumPeople(),
                dto.getOpenTalkLink(),
                author,
                MatchingStatus.WAITING
        );
        // 멤버 ID를 통해 Member 조회
        List<Member> findParticipants = dto.getParticipants().stream().map(
                (participantId) -> memberRepository.findById(participantId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + participantId))
        ).toList();
        // validation : Member
        profileMethodValidator.validateMemberOfGender(findParticipants, dto.getGender());
        // 호스트 조회 및 생성
        List<MatchingHost> matchingHosts = createMatchingHosts(matchingPost, findParticipants);
        // 매칭 글에 참가자(* 호스트) 추가
        matchingPost.addHost(matchingHosts);
        // 매칭글 영속화
        matchingPostRepository.save(matchingPost);
        return matchingPost.getId();
    }
    // 훕팅 글 수정
    @Transactional
    public Long updateMatchingPost(Long matchingPostId, Authentication authentication, UpdateMatchingPostData dto){
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingPostId));
        // 자신의 작성한 글인지 확인
        Member author = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        authUtil.isOwnerOfMatchingObject(author, matchingPost.getAuthor());
        // 멤버 ID를 통해 Member 조회
        List<Member> findParticipants = dto.getParticipants().stream().map(
                (participantId) -> memberRepository.findById(participantId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + participantId))
        ).toList();
        // validation-1 : GENDER
        profileMethodValidator.validateMemberOfGender(findParticipants, dto.getGender());
        // validation-2 : matchingPost
        matchingPostMethodValidator.validateMatchingPostStatus(matchingPost.getMatchingStatus());

        matchingPost.updateMatchingPost(dto); // 변경 감지(Dirty checking)
        // 호스트 수정
        matchingPost.updateHost(createMatchingHosts(matchingPost, findParticipants));
        return matchingPostId;
    }
    // 훕팅 글 삭제
    @Transactional
    public void removeMatchingPost(Long matchingPostId, Authentication authentication){
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingPostId));
        // 자신의 작성한 글인지 확인
        Member author = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        authUtil.isOwnerOfMatchingObject(author, matchingPost.getAuthor());
        // validation: matchingPost
        matchingPostMethodValidator.validateMatchingPostStatus(matchingPost.getMatchingStatus());
        // delete operation
        matchingPostRepository.delete(matchingPost);
    }

    // 내 매칭글 조회
    public FindMyMatchingPostResponse findMyMatchingPost(Authentication authentication){
        // 요청자 확인
        Member author = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        List<MatchingPost> findMyMatchingPosts = matchingPostQueryRepository.findByAuthor(author);
        List<FindMyMatchingPostData> findMyMatchingPostDatas = findMyMatchingPosts.stream()
                .map(FindMyMatchingPostData::toFindMyMatchingPostData)
                .toList();
        return FindMyMatchingPostResponse.builder()
                .data(findMyMatchingPostDatas)
                .build();
    }

    // 사용자 정의 메서드
    private List<MatchingHost> createMatchingHosts(MatchingPost matchingPost, List<Member> hosts){
        List<MatchingHost> matchingHosts = new ArrayList<>();
        for(Member host : hosts){
            matchingHosts.add(new MatchingHost(matchingPost, host));
        }
        return matchingHosts;
    }
}
