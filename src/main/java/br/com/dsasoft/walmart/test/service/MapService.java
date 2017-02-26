package br.com.dsasoft.walmart.test.service;

import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import br.com.dsasoft.walmart.test.model.Outline;

@Service
public class MapService {

	@Value("${mongodb.collectionName}")
	public String COLLECTION_NAME;
	
	@Value("${mongodb.db}")
	public String dbName;

	@Autowired
	private MongoClient mongoClient;

	private DB db;

	@SuppressWarnings("deprecation")
	public String saveMap(Outline map) {
		db = mongoClient.getDB(dbName);
		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(
				db.getCollection(COLLECTION_NAME), Outline.class, String.class);
		WriteResult<Outline, String> result = coll.insert(map);

		return result.getSavedId();
	}

	@SuppressWarnings("deprecation")
	public Outline retrieveMap(String name) {
		db = mongoClient.getDB(dbName);
		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(
				db.getCollection(COLLECTION_NAME), Outline.class, String.class);
		Outline result = coll.findOneById(name);

		if (result != null)
			return result;
		else
			throw new RuntimeException("Map not found");
	}
}
