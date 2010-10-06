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

import static com.pyxis.jira.monitoring.IssueObjectMother.newIssue;
import static com.pyxis.jira.monitoring.UserObjectMother.newUser;
import static org.junit.Assert.*;

public class MonitorCustomFieldActionTest {

	private MonitorCustomFieldAction action;
	private MonitorHelper monitorHelper;

	@Before
	public void init() {
		monitorHelper = new MonitorHelper();
		action = new MonitorCustomFieldAction(monitorHelper);
	}

	@Test
	public void shouldHaveNoActivity()
			throws Exception {

		action.doExecute();
		assertEquals(0, action.getActivities().size());
	}

	@Test
	public void shouldHaveActivities()
			throws Exception {

		monitorHelper.notify(newUser("fdenommee"), newIssue("TEST-1"));
		monitorHelper.notify(newUser("vthoule"), newIssue("TEST-1"));

		action.doExecute();
		assertEquals(2, action.getActivities().size());
	}
}
