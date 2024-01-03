package com.likelion.hufsting.domain.profile.service;


import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.domain.profile.repository.ProfileRepository;
import com.likelion.hufsting.domain.profile.dto.AddProfileRequest;
import com.likelion.hufsting.domain.profile.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Profile save(AddProfileRequest request) {
        return profileRepository.save(request.toEntity());
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

        profile.update(request.getName(), request.getGender(),
                request.getStudentNumber(), request.getMajor(),
                request.getMbti(), request.getContent());
        return profile;
    }

}
