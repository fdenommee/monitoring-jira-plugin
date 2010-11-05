package it.com.pyxis.jira.monitoring.rest;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class MonitoringClearer {

	private final Client client;
	private final WebResource service;

	private static MonitoringClearer instance;

	public static MonitoringClearer getInstance() {
		if (instance == null) {
			instance = new MonitoringClearer();
		}
		return instance;
	}

	private MonitoringClearer() {
		client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));

		service = client.resource("http://localhost:2990/jira/rest/monitor/1.0");
	}

	public void clearActivities() {
		service.path("clear")
				.accept(MediaType.APPLICATION_XML_TYPE)
				.get(String.class);
	}
}