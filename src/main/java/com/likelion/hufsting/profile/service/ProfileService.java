package com.likelion.hufsting.profile.service;


import com.likelion.hufsting.profile.domain.Profile;
import com.likelion.hufsting.profile.dto.AddProfileRequest;
import com.likelion.hufsting.profile.dto.UpdateProfileRequest;
import com.likelion.hufsting.profile.repository.ProfileRepository;
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
