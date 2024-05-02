package com.cogent.tweeterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TweeterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweeterServiceApplication.class, args);
	}

}
