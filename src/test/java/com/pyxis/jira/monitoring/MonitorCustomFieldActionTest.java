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
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_MUTABLEISSUE;

import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static com.pyxis.jira.monitoring.UserObjectMother.VTHOULE_USER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.jira.issue.IssueManager;

@RunWith(MockitoJUnitRunner.class)
public class MonitorCustomFieldActionTest {

	private MonitorCustomFieldAction action;
	private MonitorHelper monitorHelper;
	@Mock private IssueManager issueManager;

	@Before
	public void init() {
		monitorHelper = new MonitorHelper();
		action = new MonitorCustomFieldAction(issueManager, monitorHelper);
	}

	@Test
	public void shouldHaveNoActivity()
			throws Exception {

		when(issueManager.getIssueObject(TEST_1_MUTABLEISSUE.getId())).thenReturn(TEST_1_MUTABLEISSUE);
		action.setIssueId(TEST_1_MUTABLEISSUE.getId());
		
		action.doExecute();
		assertEquals(0, action.getActivities().size());
	}

	@Test
	public void shouldHaveActivities()
			throws Exception {

		monitorHelper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		monitorHelper.notify(VTHOULE_USER, TEST_1_ISSUE);
		
		when(issueManager.getIssueObject(TEST_1_MUTABLEISSUE.getId())).thenReturn(TEST_1_MUTABLEISSUE);
		action.setIssueId(TEST_1_MUTABLEISSUE.getId());
		
		action.doExecute();
		assertEquals(2, action.getActivities().size());
	}
}
