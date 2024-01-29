package com.likelion.hufsting.domain.matching.validation;

import com.likelion.hufsting.domain.matching.domain.MatchingAcceptance;
import com.likelion.hufsting.domain.matching.domain.MatchingStatus;
import com.likelion.hufsting.domain.matching.exception.MatchingReqException;
import com.likelion.hufsting.domain.matching.exception.MatchingPostException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchingReqMethodValidator {
    private final String PARTICIPANT_DUPLICATION_ERR_MSG = "중복된 참여자가 존재합니다.";
    private final String PARTICIPANT_AND_HOST_NUM_EQUAL_ERR_MSG = "참여자 수와 호스트 수가 일치하지 않습니다.";
    private final String PARTICIPANT_INCLUDE_REPRESENTATIVE_ERR_MSG = "참여자 목록에 대표자가 포함되어 있지 않습니다.";
    private final String ACCEPTANCE_ALREADY_ACCEPTED = "이미 수락된 요청입니다.";
    private final String ACCEPTANCE_ALREADY_REJECTED = "이미 거부된 요청입니다.";

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
