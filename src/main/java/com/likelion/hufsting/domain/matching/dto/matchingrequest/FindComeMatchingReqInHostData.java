package com.likelion.hufsting.domain.matching.dto.matchingrequest;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class FindComeMatchingReqInHostData {
    private String name;
    private String major;
    private String studentNumber;
    private LocalDate birthDay;
    private Mbti mbti;
    private String content;

    public static FindComeMatchingReqInHostData toFindMatchingReqHostData(Member member){
        FindComeMatchingReqInHostData findComeMatchingReqInHostData = new FindComeMatchingReqInHostData();
        findComeMatchingReqInHostData.setName(member.getName());
        findComeMatchingReqInHostData.setMajor(member.getMajor());
        findComeMatchingReqInHostData.setStudentNumber(member.getProfile().getStudentNumber());
        findComeMatchingReqInHostData.setBirthDay(member.getProfile().getBirthday());
        findComeMatchingReqInHostData.setMbti(member.getProfile().getMbti());
        findComeMatchingReqInHostData.setContent(member.getProfile().getContent());
        return findComeMatchingReqInHostData;
    }
}