package com.likelion.hufsting.domain.Member.dto;

import com.likelion.hufsting.domain.Member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {

    private String name;

    private String content;

}
