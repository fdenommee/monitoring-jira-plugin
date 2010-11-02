package it.com.pyxis.jira.monitoring.gadget;

import java.util.Arrays;
import java.util.List;

import com.atlassian.jira.functest.framework.FuncTestCase;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.selenium.JiraWebDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	private static final int TEST1_ISSUE_ID = 10000;
	private static final int TEST2_ISSUE_ID = 10010;
	private static final int TEST3_ISSUE_ID = 10020;
	private static final int TEST4_ISSUE_ID = 10021;
	private static final int TEST5_ISSUE_ID = 10022;

	private static final String FIRST_MONITORING_GADGET = "gadget-10011";
	private static final String SECOND_MONITORING_GADGET = "gadget-10020";

	private JiraWebDriver driver;
	private MonitoringGadget gadget;

	@Override
	protected void setUpTest() {
		administration.restoreData("it-UserMonitorGadgetTest.xml");

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

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(TEST3_ISSUE_ID);
		}};

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(0)));
	}

	public void testShouldSeeDifferentActivityBetweenGadgets() {

		navigation.issue().viewIssue("TEST-1");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("TEST-2");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(TEST1_ISSUE_ID);
		}};

		List<String> expected = Arrays.asList(ADMIN);

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(TEST2_ISSUE_ID);
		}};

		expected = Arrays.asList("fred");

		actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
	}

	public void testShouldIssueActivityRemovedOnIssueDelete() {

		navigation.issue().viewIssue("TEST-1");
		navigation.issue().viewIssue("TEST-2");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("TEST-1");
		navigation.issue().viewIssue("TEST-2");
		
		driver.gotoDashboard();

		MonitoringGadget gadget1 = new MonitoringGadget(driver, FIRST_MONITORING_GADGET);
		MonitoringGadget gadget2 = new MonitoringGadget(driver, SECOND_MONITORING_GADGET);
		List<String> expected = Arrays.asList(ADMIN, "fred");

		List<String> actual = gadget1.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
		
		actual = gadget2.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
		
		navigation.login("admin", "admin");
		navigation.issue().deleteIssue("TEST-2");
		
		driver.gotoDashboard();
		
		actual = gadget1.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));

		actual = gadget2.getUserActivities();
		assertThat(actual.size(), is(equalTo(0)));

	}

	public void testShouldSeeSameActivitiesBetweenGadgets() {

		navigation.issue().viewIssue("TEST-4");
		navigation.issue().viewIssue("TEST-5");
		navigation.login("fred", "admin");
		navigation.issue().viewIssue("TEST-4");
		navigation.issue().viewIssue("TEST-5");

		driver.gotoDashboard();

		gadget = new MonitoringGadget(driver, FIRST_MONITORING_GADGET) {{
			config(TEST4_ISSUE_ID);
		}};

		List<String> expected = Arrays.asList(ADMIN, "fred");

		List<String> actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));

		gadget = new MonitoringGadget(driver, SECOND_MONITORING_GADGET) {{
			config(TEST5_ISSUE_ID);
		}};

		actual = gadget.getUserActivities();
		assertThat(actual.size(), is(equalTo(expected.size())));
		assertThat(actual.containsAll(expected), is(true));
	}
}