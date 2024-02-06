package com.likelion.hufsting.domain.matching.util;

import org.springframework.stereotype.Component;

@Component
public class MatchingPostUtil {
    private final String BLUR_EXPRESSION = "**";
    private final int FIRST_CHAR_IN_NAME_IDX = 0;

    public String changeNameToBlurName(String rawName){
        char firstCharInName = rawName.charAt(FIRST_CHAR_IN_NAME_IDX);
        return firstCharInName + BLUR_EXPRESSION;
    }
}
