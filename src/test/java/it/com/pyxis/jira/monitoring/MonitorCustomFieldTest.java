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
package it.com.pyxis.jira.monitoring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.webtests.JIRAWebTest;

public class MonitorCustomFieldTest
		extends JIRAWebTest {

	public MonitorCustomFieldTest(String name) {
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

		gotoIssue("TEST-2");
		assertElementPresent("monitor_activity_admin");
	}

	@Test
	public void testCanFoundActivitiesForOtherUsers()
			throws Exception {

		gotoIssue("TEST-2");
		logout();
		login("fred", "admin");
		gotoIssue("TEST-2");

		assertElementPresent("monitor_activity_admin");
		assertElementPresent("monitor_activity_fred");
	}
}
