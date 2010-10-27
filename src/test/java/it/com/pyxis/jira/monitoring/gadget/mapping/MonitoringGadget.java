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

import it.com.pyxis.jira.selenium.JiraWebDriver;
import static org.junit.Assert.assertNotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MonitoringGadget {

	private JiraWebDriver driver; 
	
	@FindBy(id = "gadget_monitoring_user")
	private WebElement content;

	public MonitoringGadget(JiraWebDriver driver) {
		this.driver = driver;
	}
	
	public String getText() {
		return content.getText();
	}

	public void assertNodeByIdExists(String id) {
		assertNotNull(driver.findElement(By.id(id)));
	}
}
