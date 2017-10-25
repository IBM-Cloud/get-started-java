/*******************************************************************************
 * Copyright (c) 2017 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/ 
package wasdev.sample.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VCAPHelper {
	static String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

	public static JsonObject getCloudCredentials(String serviceName) {
		if(VCAP_SERVICES == null){
			return null;
		}
		//Convert VCAP_SERVICES String to JSON
		JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES);
		
		// Look for the VCAP key that holds the service info
		Entry<String, JsonElement> dbEntry = matchService(obj.entrySet(), serviceName);
		
		if (dbEntry == null) {
			System.out.println("VCAP_SERVICES: Could not find " + serviceName);
			return null;
		}

		obj = (JsonObject) ((JsonArray) dbEntry.getValue()).get(0);
		System.out.println("VCAP_SERVICES: Found " + (String) dbEntry.getKey());

		return (JsonObject) obj.get("credentials");
	}
	
	private static Entry<String, JsonElement> matchService(Set<Entry<String, JsonElement>> entries, String serviceName) {
		for (Entry<String, JsonElement> eachEntry : entries) {
                        // CF service with 'cloudant' in the name
			if (eachEntry.getKey().toLowerCase().contains(serviceName)) {
				return eachEntry;
			}
			// user-provided service with 'cloudant' in the name
			if (eachEntry.getKey().equals("user-provided")) {
				JsonArray upss = eachEntry.getValue().getAsJsonArray();
				for (JsonElement ups : upss) {
					String name = ups.getAsJsonObject().get("name").getAsString();
					if (name.toLowerCase().contains(serviceName)) {
						return eachEntry;
					}
				}
			}
		}
		return null;
	}
	
	public static Properties getLocalProperties(String fileName){
		Properties properties = new Properties();
		InputStream inputStream = VCAPHelper.class.getClassLoader().getResourceAsStream(fileName);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

}
