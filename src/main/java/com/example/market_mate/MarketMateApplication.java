package com.example.market_mate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarketMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketMateApplication.class, args);
	}

}
