package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.matching.util.MatchingPostUtil;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class FindComeMatchingReqInParticipantData {
    private String name;
    private String major;
    private String studentNumber;
    private LocalDate birthDay;
    private Mbti mbti;
    private String content;

    public static FindComeMatchingReqInParticipantData toFindComeMatchingReqInParticipantData(Member member){
        FindComeMatchingReqInParticipantData findComeMatchingReqInParticipantData = new FindComeMatchingReqInParticipantData();
        findComeMatchingReqInParticipantData.setName(member.getName().charAt(0) + "**");
        findComeMatchingReqInParticipantData.setMajor(member.getMajor());
        findComeMatchingReqInParticipantData.setStudentNumber(member.getProfile().getStudentNumber());
        findComeMatchingReqInParticipantData.setBirthDay(member.getProfile().getBirthday());
        findComeMatchingReqInParticipantData.setMbti(member.getProfile().getMbti());
        findComeMatchingReqInParticipantData.setContent(member.getProfile().getContent());
        return findComeMatchingReqInParticipantData;
    }
}
