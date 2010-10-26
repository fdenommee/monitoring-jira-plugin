package it.com.pyxis.jira.monitoring.gadget;

import org.openqa.selenium.WebDriver;

import com.atlassian.jira.functest.framework.FuncTestCase;
import it.com.pyxis.jira.monitoring.gadget.mapping.MonitoringGadget;
import it.com.pyxis.jira.selenium.DashboardPage;
import it.com.pyxis.jira.selenium.WebDriverHelper;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

public class UserMonitorGadgetTest
		extends FuncTestCase {

	private WebDriver driver;

	protected void setUpTest() {
		administration.restoreData("it-data.xml");

		driver = WebDriverHelper.newDriver();

		new DashboardPage(driver).loginAsAdmin();
	}

	@Override
	protected void tearDownTest() {
		driver.quit();
	}

	public void testShouldDisplayHelloInGadget() {
		MonitoringGadget monitoringGadget = new MonitoringGadget(driver);
		assertThat(monitoringGadget.getText(), containsString("Hello"));
	}
}
