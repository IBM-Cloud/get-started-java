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
import java.util.Collection;
import java.util.List;

import com.cloudant.client.api.Database;

import wasdev.sample.Visitor;

public class CloudantVisitorStore implements VisitorStore{
	
	private static Database db = null;
	
	static {
		try {
			db = CloudantConnMgr.getDB();
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
	public Visitor persist(Visitor td) {
		String id = db.save(td).getId();
		return db.find(Visitor.class, id);
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
		Visitor visitor = db.find(Visitor.class, id);
		db.remove(id, visitor.get_rev());
		
	}

	@Override
	public int count() throws Exception {
		return getAll().size();
	}

}
