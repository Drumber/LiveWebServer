package de.lars.LiveWebClient.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.lars.LiveWebClient.utils.SseEmitterList;

@RestController
public class SubscribeController {
	
	public final SseEmitterList emitters = new SseEmitterList();
	
	@GetMapping("/subscribe")
	public SseEmitter subscribe() {
		SseEmitter emitter = new SseEmitter(0L);
		emitters.add(emitter);
		
		// update clients
		updateClients("log", "Live clients: " + emitters.size());
		
		return emitter;
	}
	
	public void updateClients(String name, Object data) {
		emitters.send(SseEmitter.event().name(name).data(data));
	}
	
	@Scheduled(fixedDelay = 1000)
	public void timeUpdate() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jsonObj = mapper.createObjectNode();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		
		String time = format.format(new Date());
		jsonObj.put("time", time);
		format = new SimpleDateFormat("EEEE, dd. MMMM yyyy");
		jsonObj.put("date", format.format(new Date()));
		
		updateClients("time", jsonObj.toString());
		updateClients("page_title", "LiveWebClient | " + time);
	}
	
	/**
	 * Disable logging of async request timeout exceptions.
	 */
	@ExceptionHandler(value = AsyncRequestTimeoutException.class)
	public String asyncTimeout(AsyncRequestTimeoutException e) {
		//System.out.println("Block exception: " + e);
		return null;
	}

}
