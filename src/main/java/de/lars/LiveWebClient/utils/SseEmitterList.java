package de.lars.LiveWebClient.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A list for storing multiple SSE emitters that should receive the same data.
 * <br>
 * Inspired by: 
 * https://github.com/aliakh/demo-spring-sse/blob/master/server-web-mvc/src/main/java/demo/sse/server/web/mvc/controller/SseEmitters.java
 */
public class SseEmitterList {
	
	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<SseEmitter>();
	
	public SseEmitter add(SseEmitter emitter) {
		emitters.add(emitter);
		
		emitter.onCompletion(() -> {
			System.out.printf("Emitter completed: %s%n", emitter);
			emitters.remove(emitter);
		});
		emitter.onTimeout(() -> {
			System.out.printf("Emitter timed out: %s%n", emitter);
			emitter.complete();
			emitters.remove(emitter);
		});
		return emitter;
	}
	
	public void send(Object obj) {
		send(emitter -> emitter.send(obj));
	}
	
	public void send(SseEmitter.SseEventBuilder builder) {
		send(emitter -> emitter.send(builder));
	}
	
	private void send(SseEmitterConsumer<SseEmitter> consumer) {
		List<SseEmitter> failedEmitters = new ArrayList<SseEmitter>();
		
		emitters.forEach(emitter -> {
			try {
				consumer.accept(emitter);
			} catch(Exception e) {
				emitter.completeWithError(e);
				failedEmitters.add(emitter);
				System.out.printf("Emitter failed: %s%n", emitter);
			}
		});
		
		emitters.removeAll(failedEmitters);
	}
	
	@FunctionalInterface
	private interface SseEmitterConsumer<T> {
		void accept(T t) throws IOException;
	}
	
	public int size() {
		return emitters.size();
	}

}
