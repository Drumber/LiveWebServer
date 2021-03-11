package de.lars.LiveWebClient.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class to access API Keys stored in <code>src/main/resources/api_keys.properties</code>.
 * <br>
 * e.g.:
 * <pre>
 * OpenWeatherMap = XXXXXXXXXX
 * </pre>
 */
public class ApiKeyStorage {
	
	public final static String KEY_FILE = "api_keys.properties";
	
	private static Map<String, String> storageMap = new HashMap<String, String>();
	
	static {
		Properties prop = new Properties();
		try {
			prop.load(ApiKeyStorage.class.getResourceAsStream("/" + KEY_FILE));
			for(String key : prop.stringPropertyNames()) {
				storageMap.put(key, prop.getProperty(key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getKeyValue(String keyName) {
		return storageMap.get(keyName);
	}

}
