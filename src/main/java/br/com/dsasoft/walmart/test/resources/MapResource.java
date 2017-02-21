package br.com.dsasoft.walmart.test.resources;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.dsasoft.walmart.test.model.Outline;
import br.com.dsasoft.walmart.test.model.Result;
import br.com.dsasoft.walmart.test.model.Route;

import com.mongodb.DB;
import com.mongodb.MongoClient;

@RestController
public class MapResource {

	private DB db;
	private MongoClient mongoClient;
	private static final String COLLECTION_NAME = "walmart";

	/**
	 * Display a Map example
	 * 
	 * @return map on "application/json;charset=UTF-8" format
	 * 
	 * @author @dsasoft
	 */
	@RequestMapping(value = "/map/sample", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Outline> map() {
		Outline map = new Outline();
		map.setName("example-map");
		map.getRoutes().add(new Route("A", "B", 10));
		map.getRoutes().add(new Route("B", "D", 15));
		map.getRoutes().add(new Route("A", "C", 20));
		map.getRoutes().add(new Route("C", "D", 30));
		map.getRoutes().add(new Route("B", "E", 50));
		map.getRoutes().add(new Route("D", "E", 30));

		return new ResponseEntity<Outline>(map, HttpStatus.OK);
	}

	/**
	 * Save new Map
	 * 
	 * @param map
	 *            Map
	 * 
	 * @return Saved Id
	 * @author @dsasot
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "map/save", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<String> saveMap(@RequestBody Outline map) {

		this.mongoClient = new MongoClient("localhost", 27017);
		this.db = mongoClient.getDB("db");

		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(db.getCollection(COLLECTION_NAME),
				Outline.class, String.class);
		WriteResult<Outline, String> result = coll.insert(map);

		return new ResponseEntity<String>(result.getSavedId(), HttpStatus.OK);
	}

	/**
	 * Search for Map by given name
	 * 
	 * @param name
	 *            Map's name
	 * @author @dsasoft
	 * @return Return found Map
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "map/search/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Outline> retrieveMap(@PathVariable("name") String name) {
		this.mongoClient = new MongoClient("localhost", 27017);
		this.db = mongoClient.getDB("db");
		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(db.getCollection(COLLECTION_NAME),
				Outline.class, String.class);
		Outline result = coll.findOneById(name);
		return new ResponseEntity<Outline>(result, HttpStatus.OK);
	}

	/**
	 * Measure the shortest distance between Origin and Destiny inside already
	 * saved Map, and cost based on also given Truck's autonomy and Gas price.
	 * <br/>
	 * <br/>
	 * Following the premise that the Map's values is only one way. For example:
	 * The distance between A to B is 15, the reverse (B to A) is not
	 * necessarily true. <br/>
	 * It uses JGraphT Dijkstra algorithm implementation.
	 * 
	 * @see {@link http://jgrapht.org/}
	 * @param name
	 *            - Map's name
	 * @param autonomy
	 *            - Truck's autonomy
	 * @param gasPrice
	 *            - Gas price
	 * @param origin
	 *            - Origin name
	 * @param destination
	 *            - Destination name
	 * 
	 * @author @dsasoft
	 * @return Shorted Path and Cost on "application/json;charset=UTF-8" format
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/map/calc/{name}/{autonomy}/{gasprice}/{origin}-{destination}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Result> calcOnSavedMap(@PathVariable("name") String name,
			@PathVariable("autonomy") int autonomy, @PathVariable("gasprice") double gasPrice,
			@PathVariable("origin") String origin, @PathVariable("destination") String destination) {

		this.mongoClient = new MongoClient("localhost", 27017);
		this.db = mongoClient.getDB("db");
		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(db.getCollection(COLLECTION_NAME),
				Outline.class, String.class);
		Outline map = coll.findOneById(name);

		return calc(map, autonomy, gasPrice, origin, destination);
	}

	/**
	 * Measure the shortest distance between Origin and Destiny inside a given
	 * Map, and cost based on also given Truck's autonomy and Gas price. <br/>
	 * <br/>
	 * Following the premise that the Map's values is only one way. For example:
	 * The distance between A to B is 15, the reverse (B to A) is not
	 * necessarily true. <br/>
	 * It uses JGraphT Dijkstra algorithm implementation.
	 * 
	 * @see {@link http://jgrapht.org/}
	 * @param name
	 *            - Map's name
	 * @param autonomy
	 *            - Truck's autonomy
	 * @param gasPrice
	 *            - Gas price
	 * @param origin
	 *            - Origin name
	 * @param destination
	 *            - Destination name
	 * 
	 * @author @dsasoft
	 * @return Shorted Path and Cost on "application/json;charset=UTF-8" format
	 */
	@RequestMapping(value = "/map/calc/{autonomy}/{gasprice}/{origin}-{destination}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Result> calc(@RequestBody Outline map, @PathVariable("autonomy") int autonomy,
			@PathVariable("gasprice") double gasPrice, @PathVariable("origin") String origin,
			@PathVariable("destination") String destination) {

		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = createSimpleDirectedWeightedGraph(map);

		DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<String, DefaultWeightedEdge>(
				graph, origin, destination);

		List<DefaultWeightedEdge> shortestPath = dijkstra.getPathEdgeList();

		List<Route> shortestRoute = new LinkedList<Route>();

		for (DefaultWeightedEdge edge : shortestPath) {
			Route route = (Route) edge;
			shortestRoute.add(route);
		}

		double cost = calculateCost(dijkstra.getPathLength(), autonomy, gasPrice);

		return new ResponseEntity<Result>(new Result(shortestRoute, cost), HttpStatus.OK);
	}

	private final SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> createSimpleDirectedWeightedGraph(
			Outline map) {
		/***
		 * Following the premise that the Map's values is only one way. For
		 * example: The distance between A to B is 15, the reverse (B to A) is
		 * not necessarily true. ( A~B = 15, B~A <> 15 ).
		 */
		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		Iterator<Route> it = map.getRoutes().iterator();

		while (it.hasNext()) {
			Route route = it.next();
			graph.addVertex(route.getOrigin());
			graph.addVertex(route.getDestination());

			graph.addEdge(route.getOrigin(), route.getDestination(), route);

			graph.setEdgeWeight(route, route.getDistance());
		}

		return graph;
	}

	private double calculateCost(double pathLength, int autonomy, double gasPrice) {
		return (pathLength / autonomy) * gasPrice;
	}
}
