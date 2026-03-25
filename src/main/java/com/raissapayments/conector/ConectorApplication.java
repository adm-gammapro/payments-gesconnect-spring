package com.raissapayments.conector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = {"com.raissapayments.conector.domain.repository"})
@EntityScan(basePackages = {"com.raissa.comun.general.entity","com.raissapayments.conector.domain.entity"})
public class ConectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConectorApplication.class, args);
	}

}
