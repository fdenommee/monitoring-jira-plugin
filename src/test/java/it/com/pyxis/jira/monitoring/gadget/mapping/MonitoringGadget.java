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

import com.pyxis.jira.monitoring.rest.MonitorResource;

import it.com.pyxis.jira.selenium.Gadget;
import it.com.pyxis.jira.selenium.JiraWebDriver;

import static org.junit.Assert.*;

public class MonitoringGadget
		extends Gadget {

	public MonitoringGadget(final JiraWebDriver driver, String gadgetId) {
		super(driver, gadgetId);
	}

	public String getText() {
		return content().getText();
	}

	public void config(int project) {

		clickEditMenu();

		Select select = new Select(configProjectId());
		select.selectByValue(MonitorResource.PROJECT_PREFIX + String.valueOf(project));

		clickSaveButton();
	}

	public void assertConfig(int project) {

		clickEditMenu();

		try {
			String value = configProjectId().getValue();
			assertEquals(MonitorResource.PROJECT_PREFIX + String.valueOf(project), value);
		}
		finally {
			clickCancelButton();
		}
	}

	public List<String> getUserActivities() {
		insideFocus();

		List<String> activities = new ArrayList<String>();
		List<WebElement> elements = content().findElements(By.xpath("//tr[starts-with(@id,'monitor_activity_')]/td[1]"));
//		List<WebElement> elements = content().findElements(By.xpath(".//tr[starts-with(@id,'monitor_activity_')]/td[2]"));
		

		for (WebElement element : elements) {
			activities.add(element.getText());
		}

		return activities;
	}

	private WebElement content() {
		return driver.findElement(By.id("gadget_monitoring_user"));
	}

	private WebElement configSearchByIssue() {
		return driver.findElement(By.id("searchByIssue"));
	}

	private WebElement configIssueId() {
		return driver.findElement(By.id("issueId"));
	}

	private WebElement configProjectId() {
		return driver.findElement(By.id("projectId"));
	}
}
