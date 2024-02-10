package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FindMyMatchingPostInHostData {
    private Long id;
    private String name;
    private String major;
    private String studentNumber;
    private String age;
    private Mbti mbti;
    private String content;

    public static FindMyMatchingPostInHostData toFindMyMatchingPostInHostData(Member member){
        FindMyMatchingPostInHostData findMyMatchingPostInHostData = new FindMyMatchingPostInHostData();
        findMyMatchingPostInHostData.id = member.getId();
        findMyMatchingPostInHostData.name = member.getName();
        findMyMatchingPostInHostData.major = member.getMajor();
        findMyMatchingPostInHostData.studentNumber = member.getProfile().getStudentNumber();
        findMyMatchingPostInHostData.age = member.getProfile().getBirthday();
        findMyMatchingPostInHostData.mbti = member.getProfile().getMbti();
        findMyMatchingPostInHostData.content = member.getProfile().getContent();
        return  findMyMatchingPostInHostData;
    }
}
