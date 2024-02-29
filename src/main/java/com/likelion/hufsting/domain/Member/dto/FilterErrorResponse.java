package com.likelion.hufsting.domain.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterErrorResponse {
    private String error_name;
    private String error_message;
}
