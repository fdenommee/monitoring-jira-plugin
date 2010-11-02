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

public class Gadget {

	protected final JiraWebDriver driver;
	protected final String gadgetId;

	private Automaton automaton;

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

	@Deprecated
	public void openMenu() {
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

	private WebElement gadgetRenderBox() {
		outsideFocus();
		return driver.findElement(By.id(String.format("%s-renderbox", gadgetId)));
	}

	private void clickMenuItem(String classItem) {

		WebElement gadget = gadgetRenderBox();
		hoverOn(gadget);

		getAutomaton().perform(
				sequence(moveMouseTo(new GadgetMenuTracker(gadget)), clickMouseButton(Gestures.BUTTON1)));

		String xpath = String.format(
				".//*[@id='%s-chrome']/*/div[@class='gadget-menu']/*//li[@class='%s']/a[@class='no_target']",
				gadgetId, classItem);

		WebElement menuElement = gadget.findElement(By.xpath(xpath));
		hoverOn(menuElement);

		getAutomaton().perform(
				sequence(moveMouseTo(new MenuTracker(menuElement)), clickMouseButton(Gestures.BUTTON1)));

		insideFocus();
	}

	private void hoverOn(WebElement element) {

		if (element instanceof RenderedWebElement) {
			try {
				((RenderedWebElement)element).hover();
			}
			catch (UnsupportedOperationException ex) {
				System.err.printf("WARNING: hover not supported : %s", ex.getMessage());
			}
		}
	}

	private Automaton getAutomaton() {
		if (automaton == null) {
			automaton = new RoboticAutomaton();
		}
		return automaton;
	}

	private class MenuTracker
			implements Tracker {

		private final WebElement element;
		private Point center;

		private MenuTracker(WebElement element) {
			this.element = element;
		}

		public Point target(Point currentLocation) {
			if (center == null) {
				Point p = driver.getLocationOnScreenOnceScrolledIntoView(element);
				Dimension d = ((RenderedWebElement)element).getSize();

				center = new Point(p.x + d.width / 2, p.y + d.height / 2);
			}
			return center;
		}
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
