package com.likelion.hufsting.domain.oauth.domain;

import com.likelion.hufsting.domain.profile.domain.Profile;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor // Builder 와 NoArgsConstructor 에러 방지
@NoArgsConstructor
@ToString

public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    @Column(name="MEMBER_PW")
    private String pw;

    @OneToOne
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;



    public void changePw(String mpw) {
        this.pw = mpw;
    }

//    public APIUser(String mid, String password) {
//        this.id = mid;
//        this.pw = password;
//    }

    @Builder
    public Member(String email, String password) {
        this.id = email;
        this.pw = password;
    }


}
