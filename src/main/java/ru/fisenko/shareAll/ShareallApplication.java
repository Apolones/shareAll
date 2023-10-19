package ru.fisenko.shareAll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShareallApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareallApplication.class, args);
	}

}
