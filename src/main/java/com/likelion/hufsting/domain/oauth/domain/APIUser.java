package com.likelion.hufsting.domain.oauth.domain;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Getter
@Builder

@NoArgsConstructor
@ToString

public class APIUser {

    @Id
    private String mid;

    @Column(name="mpw")
    private String mpw;

    @Column(name = "nickname",unique = true)
    private String nickname;

    public void changePw(String mpw) {
        this.mpw = mpw;
    }

    public APIUser(String mid, String password) {
        this.mid = mid;
        this.mpw = password;
    }

    @Builder
    public APIUser(String email, String password, String nickname) {
        this.mid = email;
        this.mpw = password;
        this.nickname = nickname;
    }

    public APIUser update(String nickname) {
        this.nickname = nickname;

        return this;
    }
}
//APIUser 는 Access Key를 발급받을 때 자신의 mid와 mpw를 이용하므로 다른 정보들 없이
// 구성하였습니다.