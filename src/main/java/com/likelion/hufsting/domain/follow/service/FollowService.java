package com.likelion.hufsting.domain.follow.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.exception.MemberRequestException;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.follow.domain.Follow;
import com.likelion.hufsting.domain.follow.dto.SuccessMessage;
import com.likelion.hufsting.domain.follow.repository.FollowRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;


    @Transactional
    public SuccessMessage toggleMember(String followeeEmail, Authentication authentication) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(followeeEmail, JsonObject.class);
        String fEmail = jsonObject.get("memberEmail").getAsString();
        final String finalEmail;
        if(!followeeEmail.contains("@hufs.ac.kr")) {
            finalEmail = fEmail + "@hufs.ac.kr";
        } else {
            finalEmail = fEmail;
        }
        Optional<Member> followeeMember = memberRepository.findByEmail(finalEmail);
        Optional<Member> followMember = memberRepository.findByEmail(authentication.getName());
        if (followeeMember.get().getId().equals(followMember.get().getId())) {
            throw new MemberRequestException("본인을 팔로우 할 수 없습니다.");
        }
        if (!followMember.isPresent()) {
            throw new MemberRequestException("회원정보를 찾을 수 없습니다.");
        }
        if (!followeeMember.isPresent()) {
            throw new MemberRequestException("검색한 회원은 존재하지 않습니다.");
        }

        if (followMember.isPresent()) {
            //추가 기능(언팔로우)
            for(Follow follow : followeeMember.get().getFolloweeList()) {
                if(follow.getFollower().getId().equals(followMember.get().getId())) {

                    return new SuccessMessage("true");
                    //return true; // 언팔로우 기능 없어서 true 로 반환
//                    followeeMember.get().getFolloweeList().remove(follow);
//                    followMember.get().getFollowerList().remove(follow);
//                    follow.disconnectFollower();
//                    follow.disconnectFollowee();
//                    followRepository.delete(follow);
//                    return false;
                }
            }
            followRepository.save(Follow.builder()
                    .follower(followMember.get())
                    .followee(followeeMember.get())
                    .build());
            return new SuccessMessage("true");
        }
        else {
            throw new MemberRequestException("해당 유저가 없습니다");
        }
    }
}
