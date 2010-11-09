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
package it.com.pyxis.jira.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DashboardPage {

	private final JiraWebDriver driver;

	private final String atlassianToken;

	public DashboardPage(JiraWebDriver driver) {
		this.driver = driver;

		authenticateAsAdmin();

		atlassianToken = driver.findElement(By.id("atlassian-token")).getAttribute("content");
	}

	private void authenticateAsAdmin() {
		authenticate("admin", "admin");
	}

	private void authenticate(String username, String password) {
		String loginUrl = "login.jsp?os_destination=/secure/Dashboard.jspa";
		driver.gotoHome(loginUrl);

		if (driver.currentUrl().endsWith(loginUrl)) {
			userNameText().sendKeys(username);
			passwordText().sendKeys(password);
			loginButton().click();
		}
	}

	public void logout() {
		driver.gotoHome(String.format("/secure/Logout!default.jspa?atl_token=%s", atlassianToken));
	}

	private WebElement userNameText() {
		return driver.findElement(By.id("usernameinput"));
	}

	private WebElement passwordText() {
		return driver.findElement(By.id("os_password"));
	}

	private WebElement loginButton() {
		return driver.findElement(By.id("login"));
	}
}
