package com.b2w.desafio;

import com.b2w.desafio.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class PlanetasApplication {

	@Autowired
	private CounterService counterService;

	public static void main(String[] args) {
		SpringApplication.run(PlanetasApplication.class, args);
	}

	@PostConstruct
	private void configureCouter() {

		if(!counterService.collectionExists("Counter")) {
			counterService.createCollection();
		}

	}
}
