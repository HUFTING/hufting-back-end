package com.likelion.hufsting.domain.oauth.domain;

import com.likelion.hufsting.domain.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor // Builder 와 NoArgsConstructor 에러 방지
@NoArgsConstructor
@ToString

public class Member {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "MEMBER_ID")
    private String email;

    @Column(name="MEMBER_PW")
    private String pw;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
        this.email = email;
        this.pw = password;
    }


}
