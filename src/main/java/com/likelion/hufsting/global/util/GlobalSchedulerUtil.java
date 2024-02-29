package com.likelion.hufsting.global.util;

import com.likelion.hufsting.domain.alarm.repository.AlarmRepository;
import com.likelion.hufsting.domain.alarm.service.AlarmService;
import com.likelion.hufsting.domain.matching.service.MatchingPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalSchedulerUtil {
    private final MatchingPostService matchingPostService;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void deleteMatchingPostIncludeBadWord(){
        System.out.println("test scheduler");
        log.info("Scheduling remove matching post including bad word");
        matchingPostService.removeMatchingPostIncludeBadWord();
    }
}
