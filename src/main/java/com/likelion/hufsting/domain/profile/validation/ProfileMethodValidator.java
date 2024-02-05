package com.likelion.hufsting.domain.profile.validation;

import com.likelion.hufsting.global.domain.Gender;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.profile.exception.ProfileException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileMethodValidator {
    private final String GENDER_ERR_MSG = "매칭글의 성별과 참가자의 성별이 일치하지 않습니다.";
    private final String ALREADY_SET_UP_PROFILE_ERR_MSG = "이미 프로필이 설정되어 있습니다.";

    public void validateMemberOfGender(List<Member> members, Gender targetGender){
        for(Member member : members){
            String findGenderValue = member.getProfile().getGender().getValue();
            String targetGenderValue = targetGender.getValue();
            if(!findGenderValue.equals(targetGenderValue)){
                throw new ProfileException(GENDER_ERR_MSG);
            }
        }
    }

    public void validateAlreadySetUpProfile(Member member){
        if(member.getProfile().){
            throw new ProfileException(ALREADY_SET_UP_PROFILE_ERR_MSG);
        }
    }
}
