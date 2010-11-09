/**
 * Copyright (c) 2010 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.pyxis.jira.monitoring.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.PagerFilter;
import com.opensymphony.user.User;
import com.pyxis.jira.monitoring.MonitorHelper;
import com.pyxis.jira.monitoring.UserIssueActivity;
import com.pyxis.jira.util.velocity.VelocityRenderer;

@Path("")
public class MonitorResource {

	private final ProjectManager projectManager;
	private final SearchRequestService searchRequestService;
	private final SearchProvider searchProvider;
	private final JiraAuthenticationContext authenticationContext;
	private final VelocityRenderer velocityRenderer;
	private final MonitorHelper helper;

	public static final String PROJECT_PREFIX = "project-";
	public static final String FILTER_PREFIX = "filter-";

	public MonitorResource(ProjectManager projectManager, SearchRequestService searchRequestService,
						   VelocityRenderer velocityRenderer, JiraAuthenticationContext authenticationContext,
						   SearchProvider searchProvider, MonitorHelper helper) {
		this.projectManager = projectManager;
		this.searchRequestService = searchRequestService;
		this.authenticationContext = authenticationContext;
		this.velocityRenderer = velocityRenderer;
		this.searchProvider = searchProvider;
		this.helper = helper;
	}

	@GET
	@Path("users")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getActiveUsers(@QueryParam("projectId") String fitlerOrProjectId) {

		List<RestUserIssueActivity> activities = new ArrayList<RestUserIssueActivity>();
		List<UserIssueActivity> activitiesFound = null;

		if (fitlerOrProjectId.startsWith(PROJECT_PREFIX)) {
			Long pid = stripFilterPrefix(fitlerOrProjectId, PROJECT_PREFIX);
			activitiesFound = getActivitiesForProject(pid);
		}
		else if (fitlerOrProjectId.startsWith(FILTER_PREFIX)) {
			Long filterId = stripFilterPrefix(fitlerOrProjectId, FILTER_PREFIX);
			activitiesFound = getActivitiesForFilter(filterId);
		}

		for (UserIssueActivity activity : activitiesFound) {
			activities.add(new RestUserIssueActivity(activity.getUserName(), activity.getIssue().getId(),
													 activity.getTime().getTime()));
		}

		GenericEntity<List<RestUserIssueActivity>> entities =
				new GenericEntity<List<RestUserIssueActivity>>(activities) {
				};

		return Response.ok(entities).build();
	}

	@GET
	@Path("usershtml")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getActiveUsersHtml(@QueryParam("projectId") String fitlerOrProjectId) {

		Map<String, Object> parameters = velocityRenderer.newVelocityParameters();
		if (fitlerOrProjectId.startsWith(PROJECT_PREFIX)) {
			Long pid = stripFilterPrefix(fitlerOrProjectId, PROJECT_PREFIX);
			parameters.put("activities", getActivitiesForProject(pid));
		}
		else if (fitlerOrProjectId.startsWith(FILTER_PREFIX)) {
			Long filterId = stripFilterPrefix(fitlerOrProjectId, FILTER_PREFIX);
			parameters.put("activities", getActivitiesForFilter(filterId));
		}
		String body = velocityRenderer.render(
				"templates/plugins/monitoring/fields/view-activities.vm", parameters);

		GenericEntity<HtmlEntity> entity = new GenericEntity<HtmlEntity>(new HtmlEntity(body)) {
		};

		return Response.ok(entity).build();
	}

	@GET
	@Path("clear")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response clearActivities() {
		clearAllActivities();
		return Response.ok().build();
	}

	private void clearAllActivities() {
		helper.clear();
	}

	private List<UserIssueActivity> getActivitiesForProject(Long projectId) {
		Project project = projectManager.getProjectObj(projectId);
		return project == null ? new ArrayList<UserIssueActivity>() : helper.getActivities(project);
	}

	private List<UserIssueActivity> getActivitiesForFilter(long filterId) {
		SearchRequest searchRequest =
				searchRequestService.getFilter(new JiraServiceContextImpl(authenticationContext.getUser()), filterId);
		SearchResults srs = getSearchResults(searchRequest, authenticationContext.getUser());
		return searchRequest == null ? new ArrayList<UserIssueActivity>() : helper.getActivities(srs);
	}

	protected SearchResults getSearchResults(final SearchRequest searchRequest, final User user) {
		if (searchRequest == null) {
			return null;
		}
		try {
			return searchProvider.search(searchRequest.getQuery(), user, PagerFilter.getUnlimitedFilter());
		}
		catch (SearchException e) {
			return null;
		}
	}

	private Long stripFilterPrefix(String filterId, String prefix) {
		if (filterId.startsWith(prefix)) {
			final String numPart = filterId.substring(prefix.length());
			return Long.valueOf(numPart);
		}
		else {
			return Long.valueOf(filterId);
		}
	}
}
