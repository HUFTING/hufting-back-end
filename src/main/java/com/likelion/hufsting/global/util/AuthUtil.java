package com.likelion.hufsting.global.util;

import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.global.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    // constant
    private final String MATCHING_OBJECT_AUTH_ERR_MSG = "작성자 또는 신청자가 일치하지 않습니다.";
    private final String MATCHING_REQ_AND_POST_SAME_AUTHOR = "내가 작성한 글은 신청할 수 없습니다.";

    // 매칭글 및 매칭 요청 접근여부 확인 메서드
    public void isOwnerOfMatchingObject(Member accessMember, Member owner){
        Long accessMemberId = accessMember.getId();
        Long ownerId = owner.getId();
        if(!(ownerId.equals(accessMemberId))){
            throw new AuthException(MATCHING_OBJECT_AUTH_ERR_MSG);
        }
    }

    // 매칭글 작성자 != 매칭 신청자
    public void isNotOwnerOfMatchingObject(Member accessMember, Member owner){
        Long accessMemberId = accessMember.getId();
        Long ownerId = owner.getId();
        if(ownerId.equals(accessMemberId)){
            throw new AuthException(MATCHING_REQ_AND_POST_SAME_AUTHOR);
        }
    }
}
