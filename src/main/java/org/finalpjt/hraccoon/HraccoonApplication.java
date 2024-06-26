package org.finalpjt.hraccoon;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableBatchProcessing /* 배치 기능 활성화 */
@EnableJpaAuditing /* JPA Auditing 활성화 - BaseTimeEntity */
@SpringBootApplication
public class HraccoonApplication {

	public static void main(String[] args) {
		SpringApplication.run(HraccoonApplication.class, args);
	}

}
