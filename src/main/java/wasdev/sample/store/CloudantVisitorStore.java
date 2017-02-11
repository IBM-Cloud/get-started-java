package wasdev.sample.store;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;

import wasdev.sample.DatabaseConnMgr;
import wasdev.sample.Visitor;

public class CloudantVisitorStore implements VisitorStore{
	
	private static Database db = null;
	
	static {
		try {
			db = DatabaseConnMgr.getDB();
		} catch (Exception re) {
			re.printStackTrace();
		}
	}
	
	public Database getDB(){
		return db;
	}

	@Override
	public Collection<Visitor> getAll(){
        List<Visitor> docs;
		try {
			docs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(Visitor.class);
		} catch (IOException e) {
			return null;
		}
        return docs;
	}

	@Override
	public Visitor get(String id) {
		return db.find(Visitor.class, id);
	}

	@Override
	public void persist(Visitor td) {
		db.save(td);
	}

	@Override
	public Visitor update(String id, Visitor newVisitor) {
		Visitor visitor = db.find(Visitor.class, id);
		visitor.setName(newVisitor.getName());
		db.update(visitor);
		return db.find(Visitor.class, id);
		
	}

	@Override
	public void delete(String id) {
		db.remove(id, null);
		
	}

	@Override
	public int count() throws Exception {
		return getAll().size();
	}

}
