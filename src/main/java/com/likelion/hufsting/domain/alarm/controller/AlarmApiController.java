package com.likelion.hufsting.domain.alarm.controller;

import com.likelion.hufsting.domain.alarm.dto.FindAlarmResponse;
import com.likelion.hufsting.domain.alarm.dto.FindAlarmsResponse;
import com.likelion.hufsting.domain.alarm.exception.AlarmException;
import com.likelion.hufsting.domain.alarm.service.AlarmService;
import com.likelion.hufsting.global.dto.ErrorResponse;
import com.likelion.hufsting.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AlarmApiController {
    // constant
    private final String ALARM_ERR_MSG_KEY = "alarm";
    // repositories
    private final AlarmService alarmService;

    @GetMapping("/api/v1/alarms")
    public ResponseEntity<ResponseDto> getMyAlarms(Authentication authentication){
        try{
            log.info("Request to get all alarms");
            FindAlarmsResponse response = alarmService.findMyAlarms(authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/v1/alarms/{alarmid}")
    public ResponseEntity<ResponseDto> getMyAlarm(@PathVariable("alarmid") Long alarmId, Authentication authentication){
        try{
            log.info("Request to get the alarm-{}", alarmId);
            FindAlarmResponse response = alarmService.findMyAlarm(alarmId, authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AlarmException e){
            log.error(e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.createSingleResponseErrorMessage(
                    ALARM_ERR_MSG_KEY,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
