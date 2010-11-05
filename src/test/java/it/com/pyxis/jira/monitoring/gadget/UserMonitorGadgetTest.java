package it.com.pyxis.jira.monitoring.gadget;

import java.util.Arrays;
import java.util.List;

import com.atlassian.jira.functest.framework.FuncTestCase;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.monitoring.rest.MonitoringClearer;
import it.com.pyxis.jira.selenium.JiraWebDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	private static final int PROJECT_TEST_ID = 10000;
	private static final int PROJECT_OTHER_TEST_ID = 10010;

	private static final String FIRST_MONITORING_GADGET = "gadget-10011";
	private static final String SECOND_MONITORING_GADGET = "gadget-10020";

	private JiraWebDriver driver;
	private MonitoringGadget gadget;
	private MonitoringClearer clearer = MonitoringClearer.getInstance();

	@Override
	protected void setUpTest() {
		administration.restoreData("it-UserMonitorGadgetTest.xml");

		clearer.clearActivities();

		driver = new JiraWebDriver();
		driver.gotoDashboard().loginAsAdmin();
	}

	@Override
	protected void tearDownTest() {
		driver.quit();
	}

	public void testIssueConfigurationIsPersisted() {

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);

		gadget.config(PROJECT_TEST_ID);
		gadget.assertConfig(PROJECT_TEST_ID);

		gadget.config(PROJECT_OTHER_TEST_ID);
		gadget.assertConfig(PROJECT_OTHER_TEST_ID);
	}

	public void testShouldSeeNoActivityInAnyGadget() {

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(PROJECT_TEST_ID);
		}};

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(0)));
	}

	public void testShouldSeeDifferentActivityBetweenGadgets() {

		navigation.issue().viewIssue("TEST-1");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("OTH-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(PROJECT_TEST_ID);
		}};

		List<String> expected = Arrays.asList(ADMIN);

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(PROJECT_OTHER_TEST_ID);
		}};

		expected = Arrays.asList("fred");

		actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
	}

	public void testShouldIssueActivityRemovedOnIssueDelete() {

		navigation.issue().viewIssue("OTH-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(PROJECT_OTHER_TEST_ID);
		}};

		List<String> expected = Arrays.asList(ADMIN);

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		navigation.login("admin", "admin");
		navigation.issue().deleteIssue("OTH-1");

		driver.gotoDashboard();

		actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(0)));
	}

	public void testShouldHaveSameActivitiesBetweenGadgets() {

		navigation.issue().viewIssue("TEST-1");
		navigation.issue().viewIssue("TEST-2");

		driver.gotoDashboard();

		MonitoringGadget gadget1 = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(PROJECT_TEST_ID);
		}};

		MonitoringGadget gadget2 = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(PROJECT_TEST_ID);
		}};

		List<String> expected = Arrays.asList(ADMIN, ADMIN);

		List<String> actual = gadget1.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		actual = gadget2.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
	}

	public void testShouldSeeActivityPerProject() {

		navigation.issue().viewIssue("OTH-1");
		navigation.issue().viewIssue("TEST-1");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(PROJECT_OTHER_TEST_ID);
		}};

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(1)));
	}
}