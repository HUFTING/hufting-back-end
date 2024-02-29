package com.likelion.hufsting.domain.profile.controller;


import com.likelion.hufsting.domain.profile.dto.*;
import com.likelion.hufsting.domain.profile.exception.ProfileException;
import com.likelion.hufsting.domain.profile.service.ProfileService;
import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ProfileApiController {
    // constant
    private final String PROFILE_ERR_MSG_KEY = "profile";
    // services
    private final ProfileService profileService;

    @PostMapping("/api/v1/profile")
    public ResponseEntity<ResponseDto> postProfileRequest(@RequestBody @Valid CreateProfileRequest request, Authentication authentication) {
        try {
            log.info("Request to post profile");
            CreateProfileResponse response = profileService.saveProfile(request, authentication);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ProfileException e){
            log.error(e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.createSingleResponseErrorMessage(
                    PROFILE_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // 내 프로필 조회 API
    @GetMapping("/api/v1/profile")
    public ResponseEntity<ResponseDto> getMyProfile(Authentication authentication){
        try{
            log.info("Request to get my profile info");
            FindMyProfileResponse response = profileService.findMyProfile(authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ProfileException e){
            log.error(e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.createSingleResponseErrorMessage(
                    PROFILE_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // 내 프로필 수정 API
    @PutMapping("/api/v1/profile")
    public ResponseEntity<ResponseDto> putProfile(@RequestBody @Valid UpdateProfileRequest request, Authentication authentication) {
        try {
            log.info("Request to put my profile");
            UpdateProfileData convertedDto = UpdateProfileData.toUpdateProfileData(request);
            UpdateProfileResponse response = profileService.updateProfile(convertedDto, authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ProfileException e){
            log.error(e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.createSingleResponseErrorMessage(
                    PROFILE_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // 상대방 프로필 조회 API(* 임시 API)
//    @GetMapping("/api/v1/profile/{id}")
//    public ResponseEntity<CreateProfileResponse> findProfile(@PathVariable Long id) {
//        try {
//            log.info("Request to get profile-{}", id);
//            // * 해당 API가 사용될 경우 팔로워에 해당 ID가 존재하는지 확인 필요
//            Profile profile = profileService.findById(id);
//            CreateProfileResponse response = new CreateProfileResponse(profile);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }catch(IllegalArgumentException e){
//            log.error(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
