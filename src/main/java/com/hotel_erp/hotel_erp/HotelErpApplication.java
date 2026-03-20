package com.hotel_erp.hotel_erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelErpApplication.class, args);
	}

}
