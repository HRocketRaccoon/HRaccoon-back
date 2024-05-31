package org.finalpjt.hraccoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing /* JPA Auditing 활성화 - BaseTimeEntity */
@SpringBootApplication
public class HraccoonApplication {

	public static void main(String[] args) {
		SpringApplication.run(HraccoonApplication.class, args);
	}

	// @Bean
	// Hibernate5Module hibernate5Module() {
	// 	return new Hibernate5Module();
	// }

}
