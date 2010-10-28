package it.com.pyxis.jira.monitoring.gadget;

import com.atlassian.jira.functest.framework.FuncTestCase;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.selenium.JiraWebDriver;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	private static final int TEST1_ISSUE_ID = 10000;
	private static final int TEST2_ISSUE_ID = 10010;

	private static final String FIRST_MONITORING_GADGET = "gadget-10011";
	private static final String SECOND_MONITORING_GADGET = "gadget-10020";

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

	public void testIssueConfigurationIsPersisted() {

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);

		gadget.config(TEST2_ISSUE_ID);
		gadget.assertConfig(TEST2_ISSUE_ID);

		gadget.config(TEST1_ISSUE_ID);
		gadget.assertConfig(TEST1_ISSUE_ID);
	}

	public void testShouldSeeNoActivityInAnyGadget() {
		
		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);
		gadget.assertUserHasNoActivity(ADMIN);
	}

	public void testShouldSeeActivityInGadget() {
		
		navigation.issue().viewIssue("TEST-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);
		gadget.assertUserHasActivity(ADMIN);
		gadget.assertUserHasNoActivity("fred");

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET);
		gadget.assertUserHasNoActivity(ADMIN);
		gadget.assertUserHasNoActivity("fred");
	}

	public void testShouldSeeActivityForTwoUsersInOnlyFirstGadget() {
		
		navigation.issue().viewIssue("TEST-1");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("TEST-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);
		gadget.assertUserHasActivity(ADMIN);
		gadget.assertUserHasActivity("fred");

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET);
		gadget.assertUserHasNoActivity(ADMIN);
		gadget.assertUserHasNoActivity("fred");
	}

	public void testShouldSeeActivityInOnlyBothGadgets() {
		
		navigation.issue().viewIssue("TEST-1");
		navigation.issue().viewIssue("TEST-2");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("TEST-1");
		navigation.issue().viewIssue("TEST-2");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);
		gadget.assertUserHasActivity(ADMIN);
		gadget.assertUserHasActivity("fred");

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET);
		gadget.assertUserHasActivity(ADMIN);
		gadget.assertUserHasActivity("fred");
	}
}