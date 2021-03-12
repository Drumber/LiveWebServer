package de.lars.LiveWebClient.services;

import java.io.IOException;
import java.io.Serializable;
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
		
		fetchCurrentWeather("Berlin,DE");
		System.out.println(getReponseData());
	}
	
	public void fetchCurrentWeather(String location) {
		if(!checkInitialized()) return;
		String requestUrl = BASE_URL + String.format("weather?q=%s&appid=%s&units=%s&lang=%s", location, API_KEY, units, lang);
		JsonNode responseData = parseRequest(requestUrl);
		if(responseData == null) return;
		
		cachedData = responseData;
	}
	
	protected JsonNode parseRequest(String requestUrl) {
		Scanner scanner = null;
		try {
			URL url = new URL(requestUrl);
			//scanner = new Scanner(url.openStream());
			//String response = scanner.useDelimiter("\\Z").next();
			// TODO: hard coded response for testing
			String response = "{\"coord\":{\"lon\":13.4105,\"lat\":52.5244},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"Klarer Himmel\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":8.23,\"feels_like\":1.81,\"temp_min\":7.78,\"temp_max\":8.89,\"pressure\":1003,\"humidity\":53},\"visibility\":10000,\"wind\":{\"speed\":6.17,\"deg\":210},\"clouds\":{\"all\":0},\"dt\":1615572021,\"sys\":{\"type\":1,\"id\":1262,\"country\":\"DE\",\"sunrise\":1615526863,\"sunset\":1615568679},\"timezone\":3600,\"id\":2950159,\"name\":\"Berlin\",\"cod\":200}";
			
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
	
	public OpenWeatherMapResponse getReponseData() {
		if(cachedData != null)
			return OpenWeatherMapResponse.createFromJsonNode(cachedData);
		return null;
	}
	
	protected boolean checkInitialized() {
		if(!isInitialized) {
			System.err.println("The OpenWeatherMap service is unavailable.");
			return false;
		}
		return true;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	
	public static class OpenWeatherMapResponse implements Serializable {
		private static final long serialVersionUID = -8977151464034200661L;
		
		/** Geolocation: longitude */
		public final double longitude;
		/** Geolocation: latitude */
		public final double latitude;
		
		/** Weather condition id <a href="https://openweathermap.org/weather-conditions">OpenWeatherMap Docs</a> */
		public final int weatherId;
		/** Weather main name */
		public final String weatherMain;
		/** Weather description */
		public final String weatherDescription;
		/** Weather icon id */
		public final String weatherIcon;
		
		/** Current temperature */
		public final double temperature;
		/** Feels like temperature */
		public final double temperatureFeelsLike;
		/** Min temperature */
		public final double temperatureMin;
		/** Max temperature */
		public final double temperatureMax;
		/** Atmospheric pressure */
		public final int pressure;
		/** Humidity */
		public final int humidity;
		
		/** Wind speed */
		public final double windSpeed;
		/** Wind direction in degrees */
		public final int windDirection;
		
		/** City name */
		public final String cityName;
		
		/** Response as json object */
		public final JsonNode jsonNode;

		public OpenWeatherMapResponse(JsonNode jsonNode, double longitude, double latitude, int weatherId, String weatherMain,
				String weatherDescription, String weatherIcon, double temperature, double temperatureFeelsLike,
				double temperatureMin, double temperatureMax, int pressure, int humidity, double windSpeed,
				int windDirection, String cityName) {
			this.jsonNode = jsonNode;
			this.longitude = longitude;
			this.latitude = latitude;
			this.weatherId = weatherId;
			this.weatherMain = weatherMain;
			this.weatherDescription = weatherDescription;
			this.weatherIcon = weatherIcon;
			this.temperature = temperature;
			this.temperatureFeelsLike = temperatureFeelsLike;
			this.temperatureMin = temperatureMin;
			this.temperatureMax = temperatureMax;
			this.pressure = pressure;
			this.humidity = humidity;
			this.windSpeed = windSpeed;
			this.windDirection = windDirection;
			this.cityName = cityName;
		}
		
		protected static OpenWeatherMapResponse createFromJsonNode(JsonNode jsonNode) {
			JsonNode coord = jsonNode.get("coord");
			JsonNode weather = jsonNode.get("weather").get(0);
			JsonNode main = jsonNode.get("main");
			JsonNode wind = jsonNode.get("wind");
			return new OpenWeatherMapResponse(jsonNode,
					// coordinates
					coord.get("lon").asDouble(), coord.get("lat").asDouble(),
					// weather
					weather.get("id").asInt(), weather.get("main").asText(), weather.get("description").asText(), weather.get("icon").asText(),
					// main
					main.get("temp").asDouble(), main.get("feels_like").asDouble(), main.get("temp_min").asDouble(), main.get("temp_max").asDouble(),
					main.get("pressure").asInt(), main.get("humidity").asInt(),
					// wind
					wind.get("speed").asDouble(), wind.get("deg").asInt(),
					// name
					jsonNode.get("name").asText());
		}

		@Override
		public String toString() {
			return "OpenWeatherMapResponse [longitude=" + longitude + ", latitude=" + latitude + ", weatherId="
					+ weatherId + ", weatherMain=" + weatherMain + ", weatherDescription=" + weatherDescription
					+ ", weatherIcon=" + weatherIcon + ", temperature=" + temperature + ", temperatureFeelsLike="
					+ temperatureFeelsLike + ", temperatureMin=" + temperatureMin + ", temperatureMax=" + temperatureMax
					+ ", pressure=" + pressure + ", humidity=" + humidity + ", windSpeed=" + windSpeed
					+ ", windDirection=" + windDirection + ", cityName=" + cityName + ", jsonNode=" + jsonNode + "]";
		}
		
	}

}
