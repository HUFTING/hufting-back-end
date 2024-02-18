package com.likelion.hufsting.domain.matching.validation;

import com.likelion.hufsting.domain.matching.domain.MatchingAcceptance;
import com.likelion.hufsting.domain.matching.domain.MatchingRequest;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.exception.MatchingReqException;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MatchingReqMethodValidator {
    private final String PARTICIPANT_DUPLICATION_ERR_MSG = "중복된 참여자가 존재합니다.";
    private final String PARTICIPANT_AND_HOST_NUM_EQUAL_ERR_MSG = "참여자 수와 호스트 수가 일치하지 않습니다.";
    private final String PARTICIPANT_INCLUDE_REPRESENTATIVE_ERR_MSG = "참여자 목록에 대표자가 포함되어 있지 않습니다.";
    private final String ACCEPTANCE_ALREADY_ACCEPTED = "이미 수락된 요청입니다.";
    private final String ACCEPTANCE_ALREADY_REJECTED = "이미 거부된 요청입니다.";
    private final String NOT_EQUAL_AUTHOR_AND_ACCESS_MEMBER_ID = "조회할 수 없는 매칭 요청입니다.";
    private final String ALREADY_REQUEST_ERR_MSG = "이미 신청한 매칭글입니다.";

    // 매칭 요청 등록 및 수정시 유효성 검사 메서드
    public void validateParticipantsField(List<Long> participantIds, Long representativeId, int hostCounts){
        if(!isParticipantsAndHostsCountEqual(participantIds, hostCounts)){
            throw new MatchingReqException(PARTICIPANT_AND_HOST_NUM_EQUAL_ERR_MSG);
        }else if(!isParticipantDuplication(participantIds)){
            throw new MatchingReqException(PARTICIPANT_DUPLICATION_ERR_MSG);
        }else if(!isRepresentativeInParticipants(representativeId, participantIds)){
            throw new MatchingReqException(PARTICIPANT_INCLUDE_REPRESENTATIVE_ERR_MSG);
        }
    }

    // 매칭 요청 취소 유효성 검사 메서드
    public void validateCanBeDeleted(MatchingAcceptance matchingAcceptance){
        if(matchingAcceptance.equals(MatchingAcceptance.ACCEPTED)){
            throw new MatchingReqException(ACCEPTANCE_ALREADY_ACCEPTED);
        }else if(matchingAcceptance.equals(MatchingAcceptance.REJECTED)){
            throw new MatchingReqException(ACCEPTANCE_ALREADY_REJECTED);
        }
    }

    // 매칭 요청 수정 유효성 검사 메서드
    public void validateCanBeModified(MatchingAcceptance matchingAcceptance){
        if(matchingAcceptance.equals(MatchingAcceptance.ACCEPTED)){
            throw new MatchingReqException(ACCEPTANCE_ALREADY_ACCEPTED);
        }else if(matchingAcceptance.equals(MatchingAcceptance.REJECTED)){
            throw new MatchingReqException(ACCEPTANCE_ALREADY_REJECTED);
        }
    }

    // 나에게 온 매칭 요청 조회 시 유효성 검사 메서드
    public void validateCanAccessToComeReq(Long authorId, Long accessMemberId){
        System.out.println("1번 들어옴!!!!!");
        if(!authorId.equals(accessMemberId)){
            throw new MatchingReqException(NOT_EQUAL_AUTHOR_AND_ACCESS_MEMBER_ID);
        }
    }

    // 이미 신청한 매칭 요청인지 확인
    public void validateAlreadyRequest(Optional<MatchingRequest> matchingRequest){
        if(matchingRequest.isPresent()){
            throw new MatchingReqException(ALREADY_REQUEST_ERR_MSG);
        }
    }

    // 참여자 중복 확인
    private boolean isParticipantDuplication(List<Long> participantIds){
        return participantIds.size() == participantIds.stream().distinct().count();
    }

    // 참여자 수와 호스트 수 일치 확인
    private boolean isParticipantsAndHostsCountEqual(List<Long> participantIds, int hostCounts){
        return participantIds.size() == hostCounts;
    }

    // 참여자 목록에 대표자 포함 확인
    private boolean isRepresentativeInParticipants(Long representativeId, List<Long> participantIds){
        return participantIds.contains(representativeId);
    }
}
