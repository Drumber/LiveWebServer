package de.lars.LiveWebClient.services;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.lars.LiveWebClient.utils.ApiKeyStorage;

public class OpenWeatherMapService {
	
	private JsonNode cachedData;
	private final String API_KEY;
	private final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
	
	private String units = "metric";
	private String lang = "de";
	
	private boolean isInitialized;
	
	public OpenWeatherMapService() {
		API_KEY = ApiKeyStorage.getKeyValue("OpenWeatherMap");
		if(API_KEY == null) {
			isInitialized = false;
			System.err.println("Could not initialize OpenWeatherMap service: No API Key found!");
			return;
		}
		
		isInitialized = true;
		
		//fetchCurrentWeather("Lahr,DE");
	}
	
	public void fetchCurrentWeather(String location) {
		if(checkInitialized()) return;
		String requestUrl = BASE_URL + String.format("weather?q=%s&appid=%s&units=%s&lang=%s", location, API_KEY, units, lang);
		JsonNode responseData = parseRequest(requestUrl);
		if(responseData == null) return;
		
		System.out.println(responseData.toString());
		cachedData = responseData;
	}
	
	protected JsonNode parseRequest(String requestUrl) {
		Scanner scanner = null;
		try {
			URL url = new URL(requestUrl);
			scanner = new Scanner(url.openStream());
			String response = scanner.useDelimiter("\\Z").next();
			
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readTree(response);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
	}
	
	protected boolean checkInitialized() {
		if(!isInitialized) {
			System.err.println("The OpenWeatherMap service is unavailable.");
			return false;
		}
		return true;
	}

}
