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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.pyxis.jira.monitoring.MonitorHelper;
import com.pyxis.jira.monitoring.UserIssueActivity;

@Path("")
public class MonitorResource {

	private final MonitorHelper helper;

	public MonitorResource(MonitorHelper helper) {
		this.helper = helper;
	}

	@GET
	@AnonymousAllowed
	@Path("users")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getActiveUsers(@QueryParam("key") String key) {

		List<RestUserIssueActivity> activities = new ArrayList<RestUserIssueActivity>();

		for (UserIssueActivity activity : helper.getActivities()) {
			activities.add(new RestUserIssueActivity(activity.getUserName(), activity.getIssue().getKey(),
													 activity.getTime().getTime()));
		}

		GenericEntity<List<RestUserIssueActivity>> entities =
				new GenericEntity<List<RestUserIssueActivity>>(activities) {
				};

		return Response.ok(entities).build();
	}
}
