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
package com.pyxis.jira.monitoring;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static com.pyxis.jira.monitoring.IssueObjectMother.newIssue;
import static com.pyxis.jira.monitoring.UserObjectMother.newUser;
import static org.junit.Assert.*;

public class MonitorHelperTest {

	private MonitorHelper helper;

	@Before
	public void init() {
		helper = new MonitorHelper();
	}

	@Test
	public void activitesAreFoundForDifferentUserOnSameIssue() {

		helper.notify(newUser("fdenommee"), newIssue("TEST-1"));
		helper.notify(newUser("vthoule"), newIssue("TEST-1"));

		List<UserIssueActivity> activities = helper.getActivities();
		assertEquals(2, activities.size());
	}

	@Test
	public void shouldKeepLatestUserActivityForAnIssue() {

		helper.notify(newUser("fdenommee"), newIssue("TEST-1"));
		helper.notify(newUser("fdenommee"), newIssue("TEST-1"));

		List<UserIssueActivity> activities = helper.getActivities();
		assertEquals(1, activities.size());
	}

	@Test
	public void shouldKeepUsersActivityForDifferentIssues() {

		helper.notify(newUser("fdenommee"), newIssue("TEST-1"));
		helper.notify(newUser("fdenommee"), newIssue("TEST-2"));

		List<UserIssueActivity> activities = helper.getActivities();
		assertEquals(2, activities.size());
	}

}
