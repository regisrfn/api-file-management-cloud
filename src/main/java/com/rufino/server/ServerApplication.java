package com.rufino.server;

import com.rufino.server.service.FileStorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(FileStorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
