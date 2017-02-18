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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.dsasoft.walmart.test.model.Outline;
import br.com.dsasoft.walmart.test.model.Result;
import br.com.dsasoft.walmart.test.model.Route;

import com.mongodb.DB;
import com.mongodb.MongoClient;

@Controller
public class MapResource {

	/**
	 * Display a Map example
	 * 
	 * @return map on "application/json;charset=UTF-8" format
	 * 
	 * @author @dsasoft
	 * */
	@RequestMapping(value = "/map/sample", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Outline> map() {
		Outline map = new Outline();

		map.getRoutes().add(new Route("A", "B", 10));
		map.getRoutes().add(new Route("B", "D", 15));
		map.getRoutes().add(new Route("A", "C", 20));
		map.getRoutes().add(new Route("C", "D", 30));
		map.getRoutes().add(new Route("B", "E", 50));
		map.getRoutes().add(new Route("D", "E", 30));

		return new ResponseEntity<Outline>(map, HttpStatus.OK);
	}

	@RequestMapping(value = "map/save/{name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> saveMap(@PathVariable("name") String name, @RequestBody Outline map) {
		
		
		@SuppressWarnings({"resource", "deprecation"})
		DB db = new MongoClient("localhost" , 27017 ).getDB("db");
		JacksonDBCollection<Outline, String> coll = JacksonDBCollection.wrap(db.getCollection("coll"), Outline.class, String.class);
		WriteResult<Outline, String> result = coll.insert(map);
		
		return new ResponseEntity<String>(result.getSavedId(), HttpStatus.OK);
	}
	

	/**
	 * Measure the shortest distance between Origin and Destiny inside a given
	 * Map, and cost based on also given Truck's Autonomy and Gas price. <br/>
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
	 * */
	@RequestMapping(value = "/map/calc/{name}/{autonomy}/{gasprice}/{origin}-{destination}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Result> receiveMap(@RequestBody Outline map,
			@PathVariable("name") String name,
			@PathVariable("autonomy") int autonomy,
			@PathVariable("gasprice") double gasPrice,
			@PathVariable("origin") String origin,
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

		return new ResponseEntity<Result>(new Result(shortestRoute, cost),
				HttpStatus.OK);
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
