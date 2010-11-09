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
package it.com.pyxis.jira;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.atlassian.jira.functest.framework.Administration;
import com.atlassian.jira.functest.framework.Navigation;
import com.atlassian.jira.functest.framework.assertions.Assertions;
import it.com.pyxis.jira.monitoring.rest.MonitorRestClient;
import it.com.pyxis.jira.selenium.JiraWebDriver;

public abstract class AbstractUIIntegrationTestCase {

	public static final String ADMIN = "admin";

	private static FuncTestCaseWrapper jira;
	protected static JiraWebDriver driver;

	protected Administration administration = jira.administration();
	protected Assertions assertions = jira.assertions();
	protected Navigation navigation = jira.navigation();

	@BeforeClass
	public static void setUp() {
		jira = new FuncTestCaseWrapper();
		jira.doSetup();

		driver = new JiraWebDriver();
	}

	@AfterClass
	public static void tearDown() {
		driver.quit();
		jira.doTearDown();
		jira = null;
	}

	public static void restoreData(String resource) {
		jira.administration().restoreData(resource);
		new MonitorRestClient().clearActivities();
	}
}