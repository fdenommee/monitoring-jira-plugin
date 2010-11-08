/**
 * Copyright (c) 2010 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package it.com.pyxis.jira.monitoring.gadget.mapping;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import it.com.pyxis.jira.selenium.Gadget;
import it.com.pyxis.jira.selenium.JiraWebDriver;

import static org.junit.Assert.*;

public class MonitoringGadget
		extends Gadget {

	public MonitoringGadget(final JiraWebDriver driver, String gadgetConfigId) {
		super(driver, gadgetConfigId);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getText() {
		return content().getText();
	}

	public void config(String filterOrProject) {

		clickEditMenu();

		Select select = new Select(configFilterAndProjectId());
		select.selectByValue(filterOrProject);

		clickSaveButton();
	}

	public void assertConfig(String filterOrProject) {

		clickEditMenu();

		try {
			String value = configFilterAndProjectId().getValue();
			assertEquals(filterOrProject, value);
		}
		finally {
			clickCancelButton();
		}
	}

	public void assertNoActivity() {
		insideFocus();
		assertNotNull(driver.findElement(By.id("no_monitor_activity")));
	}

	public List<String> getUserActivities() {
		insideFocus();

		List<String> activities = new ArrayList<String>();
		List<WebElement> elements = content().findElements(By.xpath("//tr[starts-with(@id,'monitor_activity_')]/td[1]"));

		for (WebElement element : elements) {
			activities.add(element.getText());
		}

		return activities;
	}

	public List<String> getUserActivitiesByIds() {
		insideFocus();

		List<String> activities = new ArrayList<String>();
		List<WebElement> elements = content().findElements(By.xpath("//tr[starts-with(@id,'monitor_activity_')]"));

		for (WebElement element : elements) {
			activities.add(element.getAttribute("id"));
		}

		return activities;
	}

	private WebElement content() {
		return driver.findElement(By.id("gadget_" + gadgetConfigId + "_monitoring_user"));
	}

	private WebElement configFilterAndProjectId() {
		return driver.findElement(By.id("projectId"));
	}
}
