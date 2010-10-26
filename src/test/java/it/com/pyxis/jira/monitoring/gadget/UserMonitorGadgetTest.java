package it.com.pyxis.jira.monitoring.gadget;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.functest.framework.FuncTestCase;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	@Before
	protected void setUpTest() {
		administration.restoreData("it-data.xml");
	}

	@Test
	public void testShouldDisplayHelloInGadget() {

		// @todo : fix this test
		
		navigation.logout();
		navigation.login("admin", "admin");

		navigation.dashboard().navigateTo();

		assertions.assertNodeByIdExists("dashboard");
		assertions.assertNodeByIdExists("dashboard-header");

		assertions.assertNodeByIdExists("dashboard-content");
		assertions.assertNodeByIdExists("gadget-10010");

		assertions.assertNodeByIdExists("gadget_monitoring_user");
		text.assertTextPresent("gadget_monitoring_user", "Hello");
	}
}
