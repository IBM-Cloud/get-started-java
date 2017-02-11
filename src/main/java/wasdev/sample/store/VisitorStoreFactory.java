package wasdev.sample.store;

public class VisitorStoreFactory {
	
	private static VisitorStore instance;
	
	public static VisitorStore getInstance() {
		CloudantVisitorStore cvif = new CloudantVisitorStore();	
		if(cvif.getDB() == null){
			return null;
		}
		instance = cvif;
		return instance;
		
	}

}
