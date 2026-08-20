package com.example.SoccerSeason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SoccerSeasonApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoccerSeasonApplication.class, args);
	}

}
