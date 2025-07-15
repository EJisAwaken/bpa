package com.example.Bpa_v2_bakc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BpaV2BakcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpaV2BakcApplication.class, args);
	}

}
