package com.likelion.hufsting.domain.profile.controller;


import com.likelion.hufsting.domain.profile.domain.Profile;
import com.likelion.hufsting.domain.profile.dto.AddProfileRequest;
import com.likelion.hufsting.domain.profile.dto.ProfileResponse;
import com.likelion.hufsting.domain.profile.dto.UpdateProfileRequest;
import com.likelion.hufsting.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProfileApiController {

    private final ProfileService profileService;

    @PostMapping("/profiles")
    public ResponseEntity<ProfileResponse> addProfile(@RequestBody AddProfileRequest request, Authentication authentication) {
        ProfileResponse response = profileService.save(request, authentication);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/profiles/{id}")
    public ResponseEntity<ProfileResponse> findProfile(@PathVariable long id) {
        Profile profile = profileService.findById(id);
        return ResponseEntity.ok()
                .body(new ProfileResponse(profile));
    }
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        profileService.delete(id);

        return ResponseEntity.ok()
                .build();
    }
    @PutMapping("/profile/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable long id, @RequestBody UpdateProfileRequest request) {
        Profile updatedProfile = profileService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedProfile);
    }
}
