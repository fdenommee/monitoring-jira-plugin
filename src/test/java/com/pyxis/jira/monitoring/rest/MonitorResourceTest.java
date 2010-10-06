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

import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.pyxis.jira.monitoring.MonitorHelper;

import static com.pyxis.jira.monitoring.IssueObjectMother.newIssue;
import static com.pyxis.jira.monitoring.UserObjectMother.newUser;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MonitorResourceTest {

	private MonitorHelper helper;
	private MonitorResource resource;

	@Before
	public void init() {
		helper = new MonitorHelper();
		resource = new MonitorResource(helper);
	}

	@Test
	public void activeUsersAreFound() {

		helper.notify(newUser("fdenommee"), newIssue("TEST-1"));
		helper.notify(newUser("vthoule"), newIssue("TEST-1"));

		Response response = resource.getActiveUsers(null);

		List<RestUserIssueActivity> users = toListOfUsers(response);

		assertEquals(2, users.size());
	}

	@SuppressWarnings("unchecked")
	private List<RestUserIssueActivity> toListOfUsers(Response response) {
		GenericEntity<List<RestUserIssueActivity>> entities =
				(GenericEntity<List<RestUserIssueActivity>>)response.getEntity();
		return entities.getEntity();
	}
}
