package it.com.pyxis.jira.monitoring.gadget;

import com.atlassian.jira.functest.framework.FuncTestCase;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.selenium.JiraWebDriver;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	private JiraWebDriver driver;
	private MonitoringGadget gadget;

	protected void setUpTest() {
		administration.restoreData("it-data.xml");

		driver = new JiraWebDriver();

	}

	@Override
	protected void tearDownTest() {
		driver.quit();
	}

	public void testShouldDisplayHelloInGadget() {
		
		navigation.issue().viewIssue("TEST-2");
		assertions.assertNodeByIdExists("monitor_activity_admin");
		
		
		driver.gotoDashboard().loginAsAdmin();
		gadget = driver.selectGadget(MonitoringGadget.class, "gadget-10011");
		gadget.assertNodeByIdExists("monitor_activity_admin");
	}
}
