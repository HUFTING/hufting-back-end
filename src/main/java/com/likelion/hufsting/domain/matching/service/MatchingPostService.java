package com.likelion.hufsting.domain.matching.service;

import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.dto.matchingpost.*;
import com.likelion.hufsting.domain.matching.repository.MatchingPostRepository;
import com.likelion.hufsting.domain.matching.repository.query.MatchingPostQueryRepository;
import com.likelion.hufsting.domain.matching.util.MatchingPostUtil;
import com.likelion.hufsting.domain.matching.validation.MatchingPostMethodValidator;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.domain.profile.validation.ProfileMethodValidator;
import com.likelion.hufsting.global.util.AuthUtil;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
    private final MatchingPostUtil matchingPostUtil;

    // 훕팅 글 전체 조회
    public FindMatchingPostsResponse<FindMatchingPostsData> findAllMatchingPost(Pageable pageable){
        Page<MatchingPost> findMatchingPostResult = matchingPostRepository.findAll(pageable);
        List<MatchingPost> findMatchingPosts = findMatchingPostResult.getContent();
        List<FindMatchingPostsData> convertedResult = findMatchingPosts.stream()
                .map(matchingPost -> new FindMatchingPostsData(
                        matchingPost.getId(),
                        matchingPost.getTitle(),
                        matchingPost.getGender(),
                        matchingPost.getDesiredNumPeople(),
                        matchingPost.getMatchingStatus(),
                        matchingPostUtil.changeNameToBlurName(matchingPost.getAuthor().getName()),
                        matchingPost.getCreatedAt()
                ))
                .toList();
        return new FindMatchingPostsResponse<FindMatchingPostsData>(
                findMatchingPostResult.getTotalPages(),
                findMatchingPostResult.getNumber(),
                convertedResult.size(),
                convertedResult);
    }
    // 훕팅 글 상세 조회
    public FindMatchingPostResponse findByIdMatchingPost(Long matchingPostId){
        // get matching post
        MatchingPost findMatchingPost = matchingPostRepository.findById(matchingPostId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + matchingPostId));
        // create response
        List<FindMatchingPostParticipantData> participants = findMatchingPost.getMatchingHosts().stream()
                .map((matchingHost) -> {
                    Member host = matchingHost.getHost();
                    Profile hostProfile = host.getProfile();
                    return FindMatchingPostParticipantData.builder()
                            .id(host.getId())
                            .name(matchingPostUtil.changeNameToBlurName(host.getName()))
                            .major(host.getMajor())
                            .age(hostProfile.getBirthday())
                            .mbti(hostProfile.getMbti())
                            .studentNumber(hostProfile.getStudentNumber())
                            .content(hostProfile.getContent())
                            .build();
                }).toList();
        return new FindMatchingPostResponse(
                findMatchingPost.getTitle(),
                findMatchingPost.getGender(),
                findMatchingPost.getDesiredNumPeople(),
                findMatchingPost.getMatchingStatus(),
                participants
                );
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

    // 훕팅 목록 글 검색
    public List<SearchingMatchingPostResponse> findOneMatchingPost(String title) {
        System.out.println("제목 " + title);
        List<MatchingPost> matchingPosts = matchingPostRepository.findByTitleContaining(title);
        return matchingPosts.stream()
                .map(post -> new SearchingMatchingPostResponse(
                        post.getTitle(),
                        post.getDesiredNumPeople(),
                        post.getGender(),
                        post.getAuthor().getName(),
                        post.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
