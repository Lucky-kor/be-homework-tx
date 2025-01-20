package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync //비동기처리를 활성화시킴 -> @Async가 정상적으로 동작하도록 선언 (Main class에 해도 무관)
@EnableJpaAuditing
@SpringBootApplication
public class HomeworkTxApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeworkTxApplication.class, args);
	}

}
