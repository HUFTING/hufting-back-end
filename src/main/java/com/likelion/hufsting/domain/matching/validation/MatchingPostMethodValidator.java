package com.likelion.hufsting.domain.matching.validation;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.matching.domain.MatchingHost;
import com.likelion.hufsting.domain.matching.domain.MatchingPost;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import com.likelion.hufsting.domain.matching.exception.MatchingReqException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchingPostMethodValidator {
    private final String PARTICIPANT_DUPLICATION_ERR_MSG = "중복된 참여자가 존재합니다.";
    private final String PARTICIPANT_AND_HOST_NUM_EQUAL_ERR_MSG = "참여자 수와 호스트 수가 일치하지 않습니다.";
    private final String PARTICIPANT_INCLUDE_REPRESENTATIVE_ERR_MSG = "참여자 목록에 대표자가 포함되어 있지 않습니다.";
    private final String STATUS_ALREADY_COMPLETED = "이미 매칭 완료된 글입니다.";
    private final String IS_LOGIN_USER_IN_MATCHING_HOSTS_ERR_MSG = "조회할 수 없는 게시물입니다.";
    // 매칭글 상태 확인
    public void validateMatchingPostStatus(MatchingStatus matchingStatus){
        if(matchingStatus.equals(MatchingStatus.COMPLETED)){
            throw new MatchingPostException(STATUS_ALREADY_COMPLETED);
        }
    }

    public void validateParticipantsField(List<Long> participantIds, Long representativeId, int desiredNumPeople){
        if(!isParticipantsAndHostsCountEqual(participantIds, desiredNumPeople)){
            throw new MatchingPostException(PARTICIPANT_AND_HOST_NUM_EQUAL_ERR_MSG);
        }else if(!isParticipantDuplication(participantIds)){
            throw new MatchingPostException(PARTICIPANT_DUPLICATION_ERR_MSG);
        }else if(!isRepresentativeInParticipants(representativeId, participantIds)){
            throw new MatchingReqException(PARTICIPANT_INCLUDE_REPRESENTATIVE_ERR_MSG);
        }
    }

    public void validateLoginMemberInHosts(Member loginMember, MatchingPost matchingPost){
        List<MatchingHost> matchingHosts = matchingPost.getMatchingHosts();
        Long loginMemberId = loginMember.getId();
        boolean isLoginMemberInHosts = Boolean.FALSE;
        for(MatchingHost matchingHost : matchingHosts){
            if(matchingHost.getHost().getId().equals(loginMemberId)){
                isLoginMemberInHosts = Boolean.TRUE;
                break;
            }
        }
        if(isLoginMemberInHosts == Boolean.FALSE){
            throw new MatchingPostException(IS_LOGIN_USER_IN_MATCHING_HOSTS_ERR_MSG);
        }
    }

    // 참여자 중복 확인
    private boolean isParticipantDuplication(List<Long> participantIds){
        return participantIds.size() == participantIds.stream().distinct().count();
    }

    // 참여자 수와 호스트 수 일치 확인
    private boolean isParticipantsAndHostsCountEqual(List<Long> participantIds, int desiredNumPeople){
        return participantIds.size() == desiredNumPeople;
    }

    // 참여자 목록에 대표자 포함 확인
    private boolean isRepresentativeInParticipants(Long representativeId, List<Long> participantIds){
        return participantIds.contains(representativeId);
    }
}
