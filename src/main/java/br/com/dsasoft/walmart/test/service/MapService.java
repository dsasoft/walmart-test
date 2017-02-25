package br.com.dsasoft.walmart.test.service;

import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import br.com.dsasoft.walmart.test.model.Outline;

@Service
public class MapService {

	private DB db;
	private MongoClient mongoClient;
	private static final String COLLECTION_NAME = "walmart";
	
	@SuppressWarnings("deprecation")
	public MapService() {
		this.mongoClient = new MongoClient("localhost", 27017);
		this.db = mongoClient.getDB("db");
	}
	
	public String saveMap(Outline map) {
		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(db.getCollection(COLLECTION_NAME),
				Outline.class, String.class);
		WriteResult<Outline, String> result = coll.insert(map);
		
		return result.getSavedId();
	}
	
	public Outline retrieveMap(String name) {
		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(db.getCollection(COLLECTION_NAME),
				Outline.class, String.class);
		Outline result = coll.findOneById(name);
		
		if(result!=null)
			return result;
		else
			throw new RuntimeException("Map not found");
	}
}
