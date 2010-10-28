package it.com.pyxis.jira.monitoring.gadget;

import com.atlassian.jira.functest.framework.FuncTestCase;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.selenium.JiraWebDriver;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	private JiraWebDriver driver;
	private MonitoringGadget gadget;

	protected void setUpTest() {
		administration.restoreData("it-data.xml");

		driver = new JiraWebDriver();

		driver.gotoDashboard().loginAsAdmin();
	}

	@Override
	protected void tearDownTest() {
		driver.quit();
	}

	public void testIssueConfigurationIsPersisted()
			throws Exception {

		gadget = new MonitoringGadget(driver, "gadget-10011");

		gadget.config(10010);
		gadget.assertConfig(10010);

		gadget.config(10000);
		gadget.assertConfig(10000);
	}

	public void testShouldDisplayHelloInGadget() {

		navigation.issue().viewIssue("TEST-2");
		assertions.assertNodeByIdExists("monitor_activity_admin");

		gadget = new MonitoringGadget(driver, "gadget-10011");
		gadget.config(10010);
		gadget.assertNodeByIdExists("monitor_activity_admin");
	}
}
