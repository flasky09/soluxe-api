package com.hotel_erp.hotel_erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class HotelErpApplication {

	static {
		// Force JVM timezone to Africa/Nairobi (UTC+3) before Spring initializes anything.
		// This affects LocalDateTime.now(), the overstay scheduler, and all date comparisons.
		TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
	}

	public static void main(String[] args) {
		SpringApplication.run(HotelErpApplication.class, args);
	}

}
