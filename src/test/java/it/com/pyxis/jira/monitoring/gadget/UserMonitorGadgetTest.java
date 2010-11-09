package it.com.pyxis.jira.monitoring.gadget;

import java.util.Arrays;
import java.util.List;

import com.atlassian.jira.functest.framework.FuncTestCase;
import com.pyxis.jira.monitoring.rest.MonitorResource;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.monitoring.rest.MonitorRestClient;
import it.com.pyxis.jira.selenium.JiraWebDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	private static final int PROJECT_TEST_ID = 10000;
	private static final int PROJECT_OTHER_TEST_ID = 10010;

	private static final int FILTER_ALL_ISSUES_ID = 10000;

	private static final int TEST1_ISSUE_ID = 10000;
	private static final int TEST2_ISSUE_ID = 10010;
	private static final int OTHER1_ISSUE_ID = 10030;
	
	private static final String FIRST_MONITORING_GADGET = "10011";
	private static final String SECOND_MONITORING_GADGET = "10020";

	private JiraWebDriver driver;
	private MonitoringGadget gadget;

	@Override
	protected void setUpTest() {
		administration.restoreData("it-UserMonitorGadgetTest.xml");

		new MonitorRestClient().clearAndClose();

		driver = new JiraWebDriver();
		driver.gotoDashboard().loginAsAdmin();
	}

	@Override
	protected void tearDownTest() {
		driver.quit();
	}

	public void testIssueConfigurationIsPersisted() {

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);

		gadget.config(asProjectId(PROJECT_TEST_ID));
		gadget.assertConfig(asProjectId(PROJECT_TEST_ID));

		gadget.config(asProjectId(PROJECT_OTHER_TEST_ID));
		gadget.assertConfig(asProjectId(PROJECT_OTHER_TEST_ID));
	}

	public void testShouldSeeNoActivityInAnyGadget() {

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		gadget.assertNoActivity();
	}

	public void testShouldSeeDifferentActivityBetweenGadgets() {

		navigation.issue().viewIssue("TEST-1");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("OTH-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		List<String> expected = Arrays.asList("monitor_activity_" + TEST1_ISSUE_ID + "_user_" + ADMIN);

		List<String> actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_OTHER_TEST_ID));
		}};

		expected = Arrays.asList("monitor_activity_" + OTHER1_ISSUE_ID + "_user_" + "fred" );

		actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		navigation.login("admin", "admin");
		navigation.issue().deleteIssue("OTH-1");

		driver.gotoDashboard();

		gadget.assertNoActivity();
	}

	public void testShouldHaveSameActivitiesBetweenGadgets() {

		navigation.issue().viewIssue("TEST-1");
		navigation.issue().viewIssue("TEST-2");

		driver.gotoDashboard();

		MonitoringGadget gadget1 = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		MonitoringGadget gadget2 = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		List<String> expected = Arrays.asList("monitor_activity_" + TEST1_ISSUE_ID + "_user_" + ADMIN, "monitor_activity_" + TEST2_ISSUE_ID + "_user_" + ADMIN);

		List<String> actual = gadget1.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		actual = gadget2.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
	}

	public void testShouldSeeActivityPerProject() {

		navigation.issue().viewIssue("OTH-1");
		navigation.issue().viewIssue("TEST-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_OTHER_TEST_ID));
		}};

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(1)));
	}

	public void testShouldSeeActivityPerFilter() {
		navigation.issue().viewIssue("OTH-1");
		navigation.issue().viewIssue("TEST-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asFilterId(FILTER_ALL_ISSUES_ID));
		}};

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(2)));
	}

	public void testShouldSeeActivityForFilterDisplayedWithDetails() {
		navigation.issue().viewIssue("OTH-1");
		navigation.issue().viewIssue("TEST-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asFilterId(FILTER_ALL_ISSUES_ID));
		}};

		List<String> expected = Arrays.asList("monitor_activity_" + OTHER1_ISSUE_ID + "_user_" + ADMIN, "monitor_activity_" + TEST1_ISSUE_ID + "_user_" + ADMIN );
		
		List<String> actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(2)));
		assertThat(actual.containsAll(expected), is(true));
	}

	public void testShouldIssueActivityRemovedOnIssueDelete() {

		navigation.issue().viewIssue("OTH-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_OTHER_TEST_ID));
		}};

		List<String> expected = Arrays.asList("monitor_activity_" + OTHER1_ISSUE_ID + "_user_" + ADMIN);

		List<String> actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		navigation.login("admin", "admin");
		navigation.issue().deleteIssue("OTH-1");

		driver.gotoDashboard();

		gadget.assertNoActivity();
	}

	public String asFilterId(int fitlerOrProject) {
		return MonitorResource.FILTER_PREFIX + String.valueOf(fitlerOrProject);
	}

	public String asProjectId(int fitlerOrProject) {
		return MonitorResource.PROJECT_PREFIX + String.valueOf(fitlerOrProject);
	}

}