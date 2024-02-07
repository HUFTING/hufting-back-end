package com.likelion.hufsting.domain.Member.validation;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.follow.domain.Follow;
import com.likelion.hufsting.global.exception.AuthException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlobalAuthMethodValidator {
    private final String NOT_MEMBER_IN_FOLLOWEE = "해당 사용자는 팔로우하지 않은 사용자입니다.";
    public void isMemberInFollwees(List<Follow> follows, Long memberId){
        for(Follow follow : follows){
            if(follow.getFollowee().getId().equals(memberId)){
                return;
            }
        }
        throw new AuthException(NOT_MEMBER_IN_FOLLOWEE);
    }
}
