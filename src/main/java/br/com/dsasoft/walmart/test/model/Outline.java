package br.com.dsasoft.walmart.test.model;

import java.util.LinkedHashSet;

import javax.persistence.Id;

public class Outline {

	@Id
	private String name;

	private LinkedHashSet<Route> routes;

	public Outline() {
		routes = new LinkedHashSet<Route>();
	}

	public LinkedHashSet<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(LinkedHashSet<Route> routes) {
		this.routes = routes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
