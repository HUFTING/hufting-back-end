package com.likelion.hufsting.domain.matching.validation;

import com.likelion.hufsting.domain.matching.exception.MatchingReqParticipantException;

import java.util.List;

public class MatchingReqMethodValidator {
    private static final String PARTICIPANT_DUPLICATION_ERR_MSG = "중복된 참여자가 존재합니다.";
    private static final String PARTICIPANT_AND_HOST_NUM_EQUAL_ERR_MSG = "참여자 수와 호스트 수가 일치하지 않습니다.";
    private static final String PARTICIPANT_INCLUDE_REPRESENTATIVE_ERR_MSG = "참여자 목록에 대표자가 포함되어 있지 않습니다.";

    public static void validateParticipantsField(List<Long> participantIds, Long representativeId, int hostCounts){
        if(!isParticipantsAndHostsCountEqual(participantIds, hostCounts)){
            System.out.println(participantIds.size());
            System.out.println(hostCounts);
            throw new MatchingReqParticipantException(PARTICIPANT_AND_HOST_NUM_EQUAL_ERR_MSG);
        }else if(!isParticipantDuplication(participantIds)){
            throw new MatchingReqParticipantException(PARTICIPANT_DUPLICATION_ERR_MSG);
        }else if(!isRepresentativeInParticipants(representativeId, participantIds)){
            throw new MatchingReqParticipantException(PARTICIPANT_INCLUDE_REPRESENTATIVE_ERR_MSG);
        }
    }

    // 참여자 중복 확인
    private static boolean isParticipantDuplication(List<Long> participantIds){
        return participantIds.size() == participantIds.stream().distinct().count();
    }

    // 참여자 수와 호스트 수 일치 확인
    private static boolean isParticipantsAndHostsCountEqual(List<Long> participantIds, int hostCounts){
        return participantIds.size() == hostCounts;
    }

    // 참여자 목록에 대표자 포함 확인
    private static boolean isRepresentativeInParticipants(Long representativeId, List<Long> participantIds){
        return participantIds.contains(representativeId);
    }
}
