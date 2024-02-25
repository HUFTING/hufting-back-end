package com.likelion.hufsting.domain.matching.dto.matchingpost;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.domain.Mbti;
import com.likelion.hufsting.global.domain.Gender;
import lombok.Getter;

@Getter
public class UpdateMatchingPostHostData {
    private Long id;
    private String name;
    private String major;
    private Gender gender;
    private String studentNumber;
    private String age;
    private Mbti mbti;
    private String content;

    public static UpdateMatchingPostHostData toUpdateMatchingPostHostData(Member member){
        UpdateMatchingPostHostData findMatchingPostHostData = new UpdateMatchingPostHostData();
        findMatchingPostHostData.id = member.getId();
        findMatchingPostHostData.name = member.getName();
        findMatchingPostHostData.major = member.getMajor();
        findMatchingPostHostData.gender = member.getProfile().getGender();
        findMatchingPostHostData.studentNumber = member.getProfile().getStudentNumber();
        findMatchingPostHostData.age = member.getProfile().getAge();
        findMatchingPostHostData.mbti = member.getProfile().getMbti();
        findMatchingPostHostData.content = member.getProfile().getContent();
        return findMatchingPostHostData;
    }
}
