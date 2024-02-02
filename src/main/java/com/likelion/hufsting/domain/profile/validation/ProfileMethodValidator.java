package com.likelion.hufsting.domain.profile.validation;

import com.likelion.hufsting.domain.matching.domain.Gender;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.exception.ProfileException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileMethodValidator {
    private final String GENDER_ERR_MSG = "매칭글의 성별과 참가자의 성별이 일치하지 않습니다.";

    public void validateMemberOfGender(List<Member> members, Gender targetGender){
        for(Member member : members){
            String findGenderValue = member.getProfile().getGender();
            String targetGenderValue = targetGender.getValue();
            if(!findGenderValue.equals(targetGenderValue)){
                throw new ProfileException(GENDER_ERR_MSG);
            }
        }
    }
}
