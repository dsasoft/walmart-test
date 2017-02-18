package br.com.dsasoft.walmart.test.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Result {

	private Set<String> route;
	
	private double cost;
	
	public Result(List<Route> routes, double cost){
		route = new LinkedHashSet<String>();
		for(Route edge:routes){
			route.add(edge.getOrigin());
			route.add(edge.getDestination());
		}
		this.cost = cost;
	}

	public Set<String> getRoute() {
		return route;
	}

	public void setRoute(Set<String> route) {
		this.route = route;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
}
