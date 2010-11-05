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

public class Gadget {

	protected final JiraWebDriver driver;
	protected final String gadgetId;

	public Gadget(JiraWebDriver driver, String gadgetId) {
		this.driver = driver;
		this.gadgetId = gadgetId;

		insideFocus();
	}

	public void outsideFocus() {
		driver.switchTo().defaultContent();
		driver.waitForElementToAppear(By.id(gadgetId));
	}

	public void insideFocus() {
		driver.switchTo().defaultContent();
		driver.waitForElementToAppear(By.id(gadgetId));
		driver.switchTo().frame(gadgetId);
	}

	public void clickEditMenu() {
		clickMenuItem("configure");
	}

	public void clickRefreshMenu() {
		clickMenuItem("reload");
	}

	public void clickCancelButton() {
		insideFocus();
		// @todo: watch out for i18n on value
		WebElement cancelButton = driver.findElement(By.xpath(".//*/div/input[@value='Cancel']"));
		cancelButton.click();
	}

	public void clickSaveButton() {
		insideFocus();
		// @todo: watch out for i18n on value
		WebElement saveButton = driver.findElement(By.xpath(".//*/div/input[@value='Save']"));
		saveButton.click();
	}

	private void clickMenuItem(String classItem) {

		outsideFocus();
		toggleGadgetMenu();

		try {
			By xpath = By.xpath(String.format(
					".//*[@id='%s-chrome']/*/div[@class='gadget-menu']/*//li[@class='%s']/a[@class='no_target']",
					gadgetId, classItem));

			WebElement menuElement = driver.waitForElementToAppear(xpath);
			menuElement.click();
		}
		finally {
			toggleGadgetMenu();
			insideFocus();
		}
	}

	private void toggleGadgetMenu() {
		driver.executeScript(String.format(
				"AJS.$('#%s-chrome').toggleClass('gadget-hover');\n" +
				"AJS.$('#%s-renderbox').toggleClass('dropdown-active');\n" +
				"AJS.$('[id=%s-chrome] [class=aui-dropdown standard hidden]').toggleClass('hidden');\n" +
				"AJS.$('#%s-renderbox').css('z-index: 0');",
				gadgetId, gadgetId, gadgetId, gadgetId));
	}
}
