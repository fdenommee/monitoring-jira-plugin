package it.com.pyxis.jira.monitoring.gadget;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.pyxis.jira.monitoring.rest.MonitorResource;
import it.com.pyxis.jira.JiraFuncTestCase;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.monitoring.rest.MonitorRestClient;
import it.com.pyxis.jira.selenium.JiraWebDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserMonitorGadgetTest
		extends JiraFuncTestCase {

	private static final int PROJECT_TEST_ID = 10000;
	private static final int PROJECT_OTHER_TEST_ID = 10010;

	private static final int FILTER_ALL_ISSUES_ID = 10000;

	private static final int TEST1_ISSUE_ID = 10000;
	private static final int TEST2_ISSUE_ID = 10010;
	private static final int OTHER1_ISSUE_ID = 10030;
	private static final int OTHER2_ISSUE_ID = 10050;

	private static final String FIRST_MONITORING_GADGET = "10011";
	private static final String SECOND_MONITORING_GADGET = "10020";

	private static JiraWebDriver driver;

	private MonitoringGadget gadget;

	@BeforeClass
	public static void initData() {
		administration.restoreData("it-UserMonitorGadgetTest.xml");
		driver = new JiraWebDriver();
	}

	@Before
	public void resetNavigation() {
		new MonitorRestClient().clearActivities();
		navigation.login("admin", "admin");
		driver.gotoDashboard();
	}

//	@After
//	public void logout() {
//		driver.gotoDashboard().logout();
//	}

	@AfterClass
	public static void closeDriver() {
		driver.quit();
		driver = null;
	}

	@Test
	public void issueConfigurationIsPersisted() {

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);

		gadget.config(asProjectId(PROJECT_TEST_ID));
		gadget.assertConfig(asProjectId(PROJECT_TEST_ID));

		gadget.config(asProjectId(PROJECT_OTHER_TEST_ID));
		gadget.assertConfig(asProjectId(PROJECT_OTHER_TEST_ID));
	}

	@Test
	public void shouldSeeNoActivityInAnyGadget() {

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		gadget.assertNoActivity();
	}

	@Test
	public void shouldSeeDifferentActivityBetweenGadgets() {

		navigation.issue().viewIssue("TEST-1");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("OTH-1");

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		List<String> expected = Arrays.asList(buildActivityId(TEST1_ISSUE_ID, ADMIN));

		List<String> actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_OTHER_TEST_ID));
		}};

		expected = Arrays.asList(buildActivityId(OTHER1_ISSUE_ID, "fred"));

		actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
	}

	@Test
	public void shouldHaveSameActivitiesBetweenGadgets() {

		navigation.issue().viewIssue("TEST-1");
		navigation.issue().viewIssue("TEST-2");

		MonitoringGadget gadget1 = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		MonitoringGadget gadget2 = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_TEST_ID));
		}};

		List<String> expected = Arrays.asList(buildActivityId(TEST1_ISSUE_ID, ADMIN),
											  buildActivityId(TEST2_ISSUE_ID, ADMIN));

		List<String> actual = gadget1.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		actual = gadget2.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
	}

	@Test
	public void shouldSeeActivityPerProject() {

		navigation.issue().viewIssue("OTH-1");
		navigation.issue().viewIssue("TEST-1");

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_OTHER_TEST_ID));
		}};

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(1)));
	}

	@Test
	public void shouldSeeActivityPerFilter() {

		navigation.issue().viewIssue("OTH-1");
		navigation.issue().viewIssue("TEST-1");

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asFilterId(FILTER_ALL_ISSUES_ID));
		}};

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(2)));
	}

	@Test
	public void shouldSeeActivityForFilterDisplayedWithDetails() {

		navigation.issue().viewIssue("OTH-1");
		navigation.issue().viewIssue("TEST-1");

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asFilterId(FILTER_ALL_ISSUES_ID));
		}};

		List<String> expected = Arrays.asList(buildActivityId(OTHER1_ISSUE_ID, ADMIN),
											  buildActivityId(TEST1_ISSUE_ID, ADMIN));

		List<String> actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(2)));
		assertThat(actual.containsAll(expected), is(true));
	}

	@Test
	public void deletedIssueShouldHaveNoActivityOnIt() {

		navigation.issue().viewIssue("OTH-2");

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(asProjectId(PROJECT_OTHER_TEST_ID));
		}};

		List<String> expected = Arrays.asList(buildActivityId(OTHER2_ISSUE_ID, ADMIN));

		List<String> actual = gadget.getUserActivitiesByIds();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		navigation.issue().deleteIssue("OTH-2");

		gadget.clickRefreshMenu();

		gadget.assertNoActivity();
	}

	private String asFilterId(int fitlerOrProject) {
		return MonitorResource.FILTER_PREFIX + String.valueOf(fitlerOrProject);
	}

	private String asProjectId(int fitlerOrProject) {
		return MonitorResource.PROJECT_PREFIX + String.valueOf(fitlerOrProject);
	}

	private String buildActivityId(int issueId, String username) {
		return String.format("monitor_activity_%s_user_%s", issueId, username);
	}
}