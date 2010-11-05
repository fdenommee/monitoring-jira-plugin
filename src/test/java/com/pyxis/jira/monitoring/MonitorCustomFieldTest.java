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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;

import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MonitorCustomFieldTest {

	private MonitorCustomField field;
	private MonitorHelper helper;
	@Mock private JiraAuthenticationContext authenticationContext;
	@Mock private IssueManager issueManager;

	@Before
	public void init() {
		helper = new DefaultMonitorHelper();
		field = new MonitorCustomField(helper, authenticationContext);
	}

	@Test
	public void askingForValueShouldRegisterTheActualUserActivity() {

		when(authenticationContext.getUser()).thenReturn(FDENOMMEE_USER);

		field.getValueFromIssue(null, TEST_1_ISSUE);

		assertEquals(1, helper.getActivities(TEST_1_ISSUE).size());
		assertEquals("fdenommee", helper.getActivities(TEST_1_ISSUE).get(0).getUserName());
	}
}
