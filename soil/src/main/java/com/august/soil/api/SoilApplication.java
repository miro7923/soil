package com.august.soil.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * TODO : web 이하 패키지는 정리될 예정이라 신경쓰지 않아도 됨
 */
@EnableJpaAuditing
@SpringBootApplication
public class SoilApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoilApplication.class, args);
	}

}
