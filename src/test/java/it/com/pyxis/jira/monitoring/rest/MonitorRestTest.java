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

import com.atlassian.jira.functest.framework.FuncTestCase;
import com.pyxis.jira.monitoring.rest.MonitorResource;
import com.pyxis.jira.monitoring.rest.RestUserIssueActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MonitorRestTest
		extends FuncTestCase {

	private static final long PROJECT_REST = 10010;
	private static final long REST1_ISSUE_ID = 10030;

	private final MonitorRestClient client = new MonitorRestClient();

	@Override
	protected void setUpTest() {
		administration.restoreData("it-MonitorRestTest.xml");
		clearActivities();
	}

	public void testCanFoundActivityOfOurselfOnProject() {

		List<RestUserIssueActivity> actual = getActivities(PROJECT_REST);
		assertThat(actual.size(), is(equalTo(0)));

		navigation.issue().viewIssue("REST-1");
		assertions.assertNodeByIdExists("monitor_activity_admin");

		actual = getActivities(PROJECT_REST);
		assertThat(actual.size(), is(equalTo(1)));

		RestUserIssueActivity activity = actual.get(0);
		assertThat(activity.getIssueId(), is(equalTo(REST1_ISSUE_ID)));
		assertThat(activity.getName(), is(equalTo(ADMIN)));
	}

	public void testActivitiesShouldBeCleared() {

		navigation.issue().viewIssue("REST-2");

		List<RestUserIssueActivity> actual = getActivities(PROJECT_REST);
		assertThat(actual.size(), is(greaterThanOrEqualTo(1)));

		clearActivities();

		actual = getActivities(PROJECT_REST);
		assertThat(actual.size(), is(equalTo(0)));
	}

	public void testDeletingAnIssue() {

		navigation.issue().viewIssue("REST-3");
		
		List<RestUserIssueActivity> actual = getActivities(PROJECT_REST);
		assertThat(actual.size(), is(greaterThanOrEqualTo(1)));
		
		navigation.issue().deleteIssue("REST-3");
		
		actual = getActivities(PROJECT_REST);
		assertThat(actual.size(), is(equalTo(0)));
	}
	
	private String asProjectId(long fitlerOrProjectId) {
		return MonitorResource.PROJECT_PREFIX + String.valueOf(fitlerOrProjectId);
	}
	
	private List<RestUserIssueActivity> getActivities(long filterOrProjectId) {
		return client.getActivities(asProjectId(filterOrProjectId));
	}

	private void clearActivities() {
		client.clearActivities();
	}
}
