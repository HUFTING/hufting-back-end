package com.likelion.hufsting;


import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class HufstingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HufstingApplication.class, args);
	}
}
