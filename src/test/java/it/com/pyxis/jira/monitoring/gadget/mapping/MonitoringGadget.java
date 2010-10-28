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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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

	public void assertNodeByIdExists(String id) {
		assertNotNull(content().findElement(By.id(id)));
	}

	public void config(int issue) {

		openMenu();
		clickEditMenu();

		Select select = new Select(configIssueId());
		select.selectByValue(String.valueOf(issue));

		clickSaveButton();
	}

	public void assertConfig(int issue) {

		openMenu();
		clickEditMenu();

		try {
			String value = configIssueId().getValue();
			assertEquals(String.valueOf(issue), value);
		}
		finally {
			clickCancelButton();
		}
	}

	private WebElement content() {
		return findElement(By.id("gadget_monitoring_user"));
	}

	private WebElement configIssueId() {
		return findElement(By.id("issueId"));
	}
}