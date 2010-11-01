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

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.pyxis.jira.monitoring.MonitorHelper;
import com.pyxis.jira.monitoring.UserIssueActivity;
import com.pyxis.jira.util.velocity.VelocityRenderer;

@Path("")
public class MonitorResource {

	private final IssueManager issueManager;
	private final VelocityRenderer velocityRenderer;
	private final MonitorHelper helper;

	public MonitorResource(IssueManager issueManager, VelocityRenderer velocityRenderer, MonitorHelper helper) {
		this.issueManager = issueManager;
		this.velocityRenderer = velocityRenderer;
		this.helper = helper;
	}

	@GET
	@Path("users")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getActiveUsers(@QueryParam("issueId") long issueId) {

		List<RestUserIssueActivity> activities = new ArrayList<RestUserIssueActivity>();

		for (UserIssueActivity activity : getActivitiesForIssue(issueId)) {
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
	public Response getActiveUsersHtml(@QueryParam("issueId") long issueId) {

		Map<String, Object> parameters = velocityRenderer.newVelocityParameters();
		parameters.put("activities", getActivitiesForIssue(issueId));

		String body = velocityRenderer.render(
				"templates/plugins/monitoring/fields/view-activities.vm", parameters);

		GenericEntity<HtmlEntity> entity = new GenericEntity<HtmlEntity>(new HtmlEntity(body)) {
		};

		return Response.ok(entity).build();
	}

	private List<UserIssueActivity> getActivitiesForIssue(long issueId) {
		Issue issue = issueManager.getIssueObject(issueId);
		return issue == null ? new ArrayList<UserIssueActivity>() : helper.getActivities(issue);
	}
}
