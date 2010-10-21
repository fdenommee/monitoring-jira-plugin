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

import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_2_ISSUE;
import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static com.pyxis.jira.monitoring.UserObjectMother.VTHOULE_USER;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.user.User;

public class MonitorHelperTest {

	private MonitorHelper helper;

	@Before
	public void init() {
		helper = new MonitorHelper();
	}
	
	@Test
	public void shouldHaveNoActivity() {
		assertEquals(0, helper.getActivities(TEST_1_ISSUE).size());
	}

	@Test
	public void shouldRecordOneActivity() {
		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		List<UserIssueActivity> activities = helper.getActivities(TEST_1_ISSUE);
		assertUserActivities(activities, new User[] { FDENOMMEE_USER });
	}
	
	@Test
	public void activitesAreFoundForDifferentUserOnSameIssue() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(VTHOULE_USER, TEST_1_ISSUE);

		List<UserIssueActivity> activities = helper.getActivities(TEST_1_ISSUE);
		assertUserActivities(activities, new User[] { FDENOMMEE_USER, VTHOULE_USER });
	}

	@Test
	public void shouldKeepLatestUserActivityForAnIssue() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);

		List<UserIssueActivity> activities = helper.getActivities(TEST_1_ISSUE);
		assertUserActivities(activities, new User[] { FDENOMMEE_USER });
	}

	@Test
	public void shouldKeepUsersActivityForDifferentIssues() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(FDENOMMEE_USER, TEST_2_ISSUE);

		List<UserIssueActivity> activities = helper.getActivities(TEST_1_ISSUE);
		assertUserActivities(activities, new User[] { FDENOMMEE_USER});
	}
	
	@Test
	public void shouldKeepUsersActivityByIssue() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(FDENOMMEE_USER, TEST_2_ISSUE);

		List<UserIssueActivity> activities = helper.getActivities(TEST_1_ISSUE);
		assertUserActivities(activities, new User[] { FDENOMMEE_USER });
	}
	
	private void assertUserActivities(List<UserIssueActivity> expected, User[] actualUsers) {
		assertEquals("Activity count mistmatch", expected.size(), actualUsers.length);
		for (int index = 0; index < expected.size(); index++) {
			assertEquals(expected.get(index).getUserName(), actualUsers[index].getName());
		}
	}	
}
