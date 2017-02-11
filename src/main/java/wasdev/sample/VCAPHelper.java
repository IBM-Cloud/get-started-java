package wasdev.sample;

import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VCAPHelper {
	static String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

	public static JsonObject getCredentials(String serviceName) {
		//Convert VCAP_SERVICES String to JSON
		JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES);		
		Entry<String, JsonElement> dbEntry = null;
		Set<Entry<String, JsonElement>> entries = obj.entrySet();
		
		// Look for the VCAP key that holds the service info
		for (Entry<String, JsonElement> eachEntry : entries) {
			if (eachEntry.getKey().toLowerCase().contains(serviceName)) {
				dbEntry = eachEntry;
				break;
			}
		}
		if (dbEntry == null) {
			System.out.println("VCAP_SERVICES: Could not find " + serviceName);
			return null;
		}

		obj = (JsonObject) ((JsonArray) dbEntry.getValue()).get(0);
		System.out.println("VCAP_SERVICES: Found " + (String) dbEntry.getKey());

		return (JsonObject) obj.get("credentials");
	}

}
