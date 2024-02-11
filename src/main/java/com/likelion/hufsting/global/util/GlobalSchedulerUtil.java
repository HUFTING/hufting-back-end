package com.likelion.hufsting.global.util;

import com.likelion.hufsting.domain.alarm.repository.AlarmRepository;
import com.likelion.hufsting.domain.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalSchedulerUtil {
    private final AlarmService alarmService;
    @Scheduled(cron = "0 0 0 1 * *")
    public void deleteAlarm(){
        alarmService.removeOverOneMonth();
    }
}
