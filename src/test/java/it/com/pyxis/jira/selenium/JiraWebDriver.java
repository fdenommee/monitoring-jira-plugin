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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import static it.com.pyxis.jira.selenium.WebDriverFactory.DEFAULT_TIMEOUT_IN_SECONDS;
import static org.junit.Assert.*;

public class JiraWebDriver {

	private final WebDriver driver;
	private final String homeUrl;
	private final WebDriverWait wait;

	public JiraWebDriver() {
		WebDriverFactory factory = new WebDriverFactory();
		homeUrl = factory.homeUrl();
		driver = factory.newDriver();
		wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_IN_SECONDS);
	}

	public WebDriver.TargetLocator switchTo() {
		return driver.switchTo();
	}

	public void gotoHome() {
		driver.navigate().to(homeUrl);
	}

	public DashboardPage gotoDashboard() {
		return new DashboardPage(this);
	}

	public void gotoIssue(String issueKey) {
		driver.navigate().to(String.format("%s/browse/%s", homeUrl, issueKey));
	}

	public WebElement findElement(By by) {
		try {
			return driver.findElement(by);
		}
		catch (NoSuchElementException ex) {
			return null;
		}
	}

	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	public WebElement waitForElementToAppear(By by) {
		return wait.until(elementAppear(by));
	}

	public void waitForElementToDisappear(By by) {
		wait.until(elementDisappear(by));
	}

	public boolean isVisible(WebElement element) {
		if (element != null) {
			try {
				return ((RenderedWebElement)element).isDisplayed();
			}
			catch (StaleElementReferenceException e) {
				return false;
			}
		}
		return false;
	}

	public <T> T with(Class<T> pageClass) {
		T page = instanciatePage(pageClass);
		initElements(page);
		return page;
	}

	public void initElements(Object page) {
		PageFactory.initElements(driver, page);
	}

	public void quit() {
		driver.quit();
	}

	private Function<WebDriver, WebElement> elementAppear(final By by) {
		return new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver from) {
				WebElement element = from.findElement(by);
				return isVisible(element) ? element : null;
			}
		};
	}

	private Function<WebDriver, Boolean> elementDisappear(final By by) {
		return new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver from) {
				WebElement element = from.findElement(by);
				return !isVisible(element);
			}
		};
	}

	private <T> T instanciatePage(Class<T> pageClass) {
		try {
			try {
				Constructor<T> ctr = pageClass.getConstructor(JiraWebDriver.class);
				return ctr.newInstance(this);
			}
			catch (NoSuchMethodException ex) {
				return pageClass.newInstance();
			}
		}
		catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
		catch (InstantiationException ex) {
			throw new RuntimeException(ex);
		}
		catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void executeScript(String script) {
		assertTrue("Driver does not support Javascript execution", driver instanceof JavascriptExecutor);
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		assertTrue("Javascript not enabled", executor.isJavascriptEnabled());
		executor.executeScript(script);
	}
}
