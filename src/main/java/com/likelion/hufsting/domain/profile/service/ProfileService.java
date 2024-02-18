package com.likelion.hufsting.domain.profile.service;


import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.domain.profile.dto.*;
import com.likelion.hufsting.domain.profile.exception.ProfileException;
import com.likelion.hufsting.domain.profile.repository.ProfileRepository;
import com.likelion.hufsting.domain.profile.validation.ProfileMethodValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProfileService {
    // constant
    private final String PROFILE_NOT_FOUND_MSG = "프로필을 설정해주세요.";
    // repositories
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    // validators
    private final ProfileMethodValidator profileMethodValidator;

    @Transactional
    public CreateProfileResponse saveProfile(CreateProfileRequest request, Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // validation-0 : 프로필 설정 확인
        profileMethodValidator.validateAlreadySetUpProfile(member);
        // generation profile
        Profile profile = request.toEntity(member);
        member.setUpProfile(profile);
        // change profile status
        member.changeProfileSetUpStatus();
        profileRepository.save(profile);
        return new CreateProfileResponse(profile);
    }

    public FindMyProfileResponse findMyProfile(Authentication authentication){
        // get login member
        Member loginMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ProfileException("Not Found: " + authentication.getName()));
        // find profile of login member
        Profile findProfile = loginMember.getProfile();
        return new FindMyProfileResponse(loginMember, findProfile);
    }

    @Transactional
    public Profile findById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + id));
    }
    public void delete(long id) {
        profileRepository.deleteById(id);
    }

    @Transactional
    public UpdateProfileResponse updateProfile(UpdateProfileData dto, Authentication authentication) {
        // get login Member
        Member loginMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ProfileException("Not Found: " + authentication.getName()));
        // get profile of login member
        Profile findProfile = loginMember.getProfile();
        // update profile
        findProfile.update(dto);
        return UpdateProfileResponse.builder()
                .gender(findProfile.getGender())
                .mbti(findProfile.getMbti())
                .studentNumber(findProfile.getStudentNumber())
                .content(findProfile.getContent())
                .age(findProfile.getAge())
                .build();
    }
}
