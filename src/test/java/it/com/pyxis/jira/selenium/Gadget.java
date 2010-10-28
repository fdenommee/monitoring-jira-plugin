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

import java.awt.Dimension;
import java.awt.Point;

import org.openqa.selenium.By;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.WebElement;

import com.objogate.wl.Automaton;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.gesture.Tracker;
import com.objogate.wl.robot.RoboticAutomaton;

import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.Gestures.moveMouseTo;
import static com.objogate.wl.gesture.Gestures.sequence;
import static org.junit.Assert.*;

public class Gadget {

	private final JiraWebDriver driver;
	private final String gadgetId;

	private Automaton automaton;

	public Gadget(JiraWebDriver driver, String gadgetId) {
		this.driver = driver;
		this.gadgetId = gadgetId;

		insideFocus();
	}

	public JiraWebDriver driver() {
		return driver;
	}

	public WebElement findElement(By by) {
		return driver.findElement(by);
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

	public void openMenu() {
		WebElement gadget = gadgetRenderBox();
		getAutomaton().perform(
				sequence(
						moveMouseTo(new GadgetMenuTracker(gadget)),
						clickMouseButton(Gestures.BUTTON1)));
	}

	public void closeMenu() {
		outsideFocus();
		getAutomaton().perform(
				moveMouseTo(new Tracker() {
					public Point target(Point currentLocation) {
						return new Point(0, 0);
					}
				}));
	}

	public void clickEditMenu() {

		WebElement gadget = gadgetRenderBox();

		WebElement gadgetHeader = dashboardItemHeader(gadget);
		assertNotNull(gadgetHeader);

		WebElement gadgetMenu = gadgetMenu(gadgetHeader);
		assertNotNull(gadgetMenu);

		WebElement configMenu = gadgetMenu.findElement(By.className("configure"));
		assertNotNull(configMenu);

		WebElement configAnchor = noTarget(configMenu);
		assertNotNull(configAnchor);

		configAnchor.click();

		insideFocus();
	}

	public void clickRefreshMenu() {

		WebElement gadget = gadgetRenderBox();

		WebElement gadgetHeader = dashboardItemHeader(gadget);
		assertNotNull(gadgetHeader);

		WebElement gadgetMenu = gadgetMenu(gadgetHeader);
		assertNotNull(gadgetMenu);

		WebElement configMenu = gadgetMenu.findElement(By.className("reload"));
		assertNotNull(configMenu);

		WebElement refreshAnchor = noTarget(configMenu);
		assertNotNull(refreshAnchor);

		refreshAnchor.click();

		insideFocus();
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

	private WebElement gadgetRenderBox() {
		outsideFocus();
		return driver.findElement(By.id(String.format("%s-renderbox", gadgetId)));
	}

	private WebElement noTarget(WebElement parent) {
		return parent.findElement(By.className("no_target"));
	}

	private WebElement gadgetMenu(WebElement parent) {
		return parent.findElement(By.className("gadget-menu"));
	}

	private WebElement dashboardItemHeader(WebElement parent) {
		return parent.findElement(By.className("dashboard-item-header"));
	}

	private Automaton getAutomaton() {
		if (automaton == null) {
			automaton = new RoboticAutomaton();
		}
		return automaton;
	}

	private class GadgetMenuTracker
			implements Tracker {

		private final WebElement element;
		private Point target;

		private GadgetMenuTracker(WebElement element) {
			this.element = element;
		}

		public Point target(Point currentLocation) {

			if (target == null) {
				Point p = driver.getLocationOnScreenOnceScrolledIntoView(element);
				Dimension d = ((RenderedWebElement)element).getSize();
				target = new Point(p.x + d.width - 18, p.y + 15); //pic size is 13x13
			}

			return target;
		}
	}
}
