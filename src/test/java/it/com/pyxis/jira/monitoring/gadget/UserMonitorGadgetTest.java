package it.com.pyxis.jira.monitoring.gadget;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.webtests.JIRAWebTest;

public class UserMonitorGadgetTest extends JIRAWebTest {

	public UserMonitorGadgetTest(String name) {
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
	public void testShouldDisplayHelloInGadget() {
		login(ADMIN_USERNAME, ADMIN_PASSWORD);
		gotoIssue("TEST-1");
		gotoDashboard();
//		try {
//			wait(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		assertElementPresent("dashboard");
		getPage().toString();
		assertElementPresent("dashboard-header");
		
//		assertElementPresent("dashboard-content");
		assertElementPresent("gadget-10010");
		
		
		assertElementPresent("gadget_monitoring_user");
		this.assertTextInElement("gadget_monitoring_user", "Hello");
	}
}
