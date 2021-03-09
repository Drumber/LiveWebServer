package de.lars.LiveWebClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LiveWebClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveWebClientApplication.class, args);
	}

}
