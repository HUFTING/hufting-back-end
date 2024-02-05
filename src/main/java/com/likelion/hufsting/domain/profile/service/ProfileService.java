package com.likelion.hufsting.domain.profile.service;


import com.likelion.hufsting.domain.Member.domain.Member;
import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.domain.profile.dto.ProfileResponse;
import com.likelion.hufsting.domain.profile.repository.ProfileRepository;
import com.likelion.hufsting.domain.profile.dto.AddProfileRequest;
import com.likelion.hufsting.domain.profile.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ProfileResponse save(AddProfileRequest request, Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + authentication.getName()));
        // generation profile
        Profile profile = request.toEntity(member);
        member.setUpProfile(profile);
        // change profile status
        member.changeProfileSetUpStatus();
        profileRepository.save(profile);
        return new ProfileResponse(profile);
    }
    public Profile findById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
    public void delete(long id) {
        profileRepository.deleteById(id);
    }

    @Transactional
    public Profile update(long id, UpdateProfileRequest request) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        profile.update(request.getGender(),
                request.getStudentNumber(), request.getMbti(),
                request.getBirthday(),
                request.getContent());
        return profile;
    }

}
