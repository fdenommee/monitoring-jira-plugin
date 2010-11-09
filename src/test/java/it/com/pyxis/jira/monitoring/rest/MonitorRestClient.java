package it.com.pyxis.jira.monitoring.rest;

import java.util.List;
import javax.ws.rs.core.MediaType;

import com.pyxis.jira.monitoring.rest.RestUserIssueActivity;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class MonitorRestClient {

	private final Client client;
	private final WebResource service;

	public MonitorRestClient() {
		client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));

		service = client.resource("http://localhost:2990/jira/rest/monitor/1.0");
	}

	public void clearActivities() {
		service.path("clear")
				.accept(MediaType.APPLICATION_XML_TYPE)
				.get(String.class);
	}

	public List<RestUserIssueActivity> getActivities(String filterOrProjectId) {
		return service.path("users")
				.queryParam("projectId", filterOrProjectId)
				.accept(MediaType.APPLICATION_XML_TYPE)
				.get(new GenericType<List<RestUserIssueActivity>>() {
				});
	}
}