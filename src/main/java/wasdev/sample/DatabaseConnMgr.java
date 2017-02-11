package wasdev.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DatabaseConnMgr {

	private static CloudantClient cloudant = null;
	private static Database db = null;

	private static String databaseName = "mydb";

	private static String url  = null;

	static {
		cloudant = createClient();
	}

	private static CloudantClient createClient() {
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		String serviceName = null;

		if (VCAP_SERVICES != null) {
			// When running in Bluemix, the VCAP_SERVICES env var will have the credentials for all bound/connected services
			// Parse the VCAP JSON structure looking for cloudant.
			
			url = VCAPHelper.getCredentials("cloudant").get("url").getAsString();

		} else {
			System.out.println("VCAP_SERVICES env var doesn't exist: running locally. Looking for credentials in cloudant.properties");
			Properties properties = new Properties();
			InputStream inputStream = DatabaseConnMgr.class.getClassLoader().getResourceAsStream("cloudant.properties");
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			url = properties.getProperty("cloudant_url");
			if(url.length()==0){
				System.out.println("To use a database, set the Cloudant url in src/main/resources/cloudant.properties");
				return null;
			}
		}

		try {
			System.out.println("Connecting to Cloudant");

			CloudantClient client = ClientBuilder.url(new URL(url)).build();
			
			return client;
		} catch (Exception e) {
			System.out.println("Unable to connect to database");
			//e.printStackTrace();
			return null;
		}
	}


	public static Database getDB() {
		if (cloudant == null) {
			return null;
		}

		if (db == null) {
			try {
				db = cloudant.database(databaseName, true);
			} catch (Exception e) {
				throw new RuntimeException("DB Not found", e);
			}
		}
		return db;
	}

	private DatabaseConnMgr() {
	}
}
