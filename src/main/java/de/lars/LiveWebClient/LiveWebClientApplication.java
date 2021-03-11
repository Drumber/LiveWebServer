package de.lars.LiveWebClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.lars.LiveWebClient.services.OpenWeatherMapService;

@SpringBootApplication
@EnableScheduling
public class LiveWebClientApplication {
	
	private OpenWeatherMapService owmService;

	public static void main(String[] args) {
		SpringApplication.run(LiveWebClientApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void afterLaunch() {
		owmService = new OpenWeatherMapService();
	}

}
