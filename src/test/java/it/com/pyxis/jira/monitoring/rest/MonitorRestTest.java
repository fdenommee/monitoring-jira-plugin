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
package it.com.pyxis.jira.monitoring.rest;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.webtests.JIRAWebTest;
import com.pyxis.jira.monitoring.rest.RestUserIssueActivity;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class MonitorRestTest
		extends JIRAWebTest {

	private Client restClient = Client.create();

	public MonitorRestTest(String name) {
		super(name);
	}

	@Before
	public void setUp() {
		super.setUp();
		restoreData("it-data.xml");
	}

	@After
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCanFoundActivityOfOurselfOnIssue()
			throws Exception {

		assertEquals(0, getActivities().size());

		gotoIssue("TEST-1");

		assertEquals(1, getActivities().size());
		assertElementPresent("monitor_activity_admin");
	}

	private List<RestUserIssueActivity> getActivities() {
		WebResource resource = restClient.resource("http://localhost:2990/jira/rest/monitor/1.0/users.xml");
		return resource.get(new GenericType<List<RestUserIssueActivity>>() {
		});
	}
}
