package br.com.dsasoft.walmart.test.model;

import java.util.LinkedHashSet;

public class Outline {

	private LinkedHashSet<Route> routes;
	
	public Outline(){
		routes = new LinkedHashSet<Route>();
	}

	public LinkedHashSet<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(LinkedHashSet<Route> routes) {
		this.routes = routes;
	}
}
