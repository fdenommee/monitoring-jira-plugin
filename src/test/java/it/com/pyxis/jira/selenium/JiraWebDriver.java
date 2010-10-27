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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.Speed;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public class JiraWebDriver {

	private static final int DEFAULT_TIMEOUT_IN_SECONDS = 5;

	private final Properties driverProperties;
	private final WebDriver driver;
	private final String homeUrl;

	public JiraWebDriver() {
		driverProperties = loadPropertiesFromLocalTestResource();
		homeUrl = buildHomeUrl(driverProperties);
		driver = newDriver();
	}

	public void gotoHome() {
		driver.navigate().to(homeUrl);
	}

	public DashboardPage gotoDashboard() {
		gotoHome();
		return new DashboardPage(this);
	}

	public WebElement findElement(By by) {
		try {
			return driver.findElement(by);
		}
		catch (NoSuchElementException ex) {
			return null;
		}
	}

	public WebElement waitForElementToAppear(By by) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_IN_SECONDS);
		return wait.until(elementAppear(by));
	}

	public void waitForElementToDisappear(By by) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_IN_SECONDS);
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
		PageFactory.initElements(driver, page);
		return page;
	}

	public <T> T selectGadget(Class<T> gadgetClass, String frameId) {
		selectGadgetFrame(frameId);
		return with(gadgetClass);
	}

	public void selectGadgetFrame(String frameId) {
		waitForElementToAppear(By.id(frameId));
		driver.switchTo().frame(frameId);
	}

	public void quit() {
		driver.quit();
	}

	@SuppressWarnings("unchecked")
	private WebDriver newDriver() {

		String driverClassName = driverProperties.getProperty(
				"driver.class", "org.openqa.selenium.firefox.FirefoxDriver");

		try {
			Class<WebDriver> driverClass = ClassUtils.getClass(driverClassName);
			WebDriver driver = driverClass.newInstance();

			setDriverSpeed(driver);
			setTimeout(driver);

			return driver;
		}
		catch (Exception ex) {
			throw new RuntimeException(String.format("Cannot instanciate driver class '%s'!", driverClassName), ex);
		}
	}

	private Properties loadPropertiesFromLocalTestResource() {

		Properties properties = new Properties();
		InputStream is = JiraWebDriver.class.getResourceAsStream("/localtest.properties");

		try {
			properties.load(is);
		}
		catch (IOException ex) {
			// fallback to default properties
		}
		finally {
			IOUtils.closeQuietly(is);
		}

		return properties;
	}

	private String buildHomeUrl(Properties properties) {

		StringBuilder url = new StringBuilder();

		url.append(properties.getProperty("jira.protocol", "http"))
				.append("://").append(properties.getProperty("jira.host", "localhost"))
				.append(":").append(properties.getProperty("jira.port", "2990"))
				.append(properties.getProperty("jira.context"));

		return url.toString();
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

	private void setDriverSpeed(WebDriver driver) {
		try {
			driver.manage().setSpeed(Speed.FAST);
		}
		catch (Exception ex) {
			// nothing to do
		}
	}

	private void setTimeout(WebDriver driver) {
		try {
			driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
		}
		catch (Exception ex) {
			// nothing to do
		}
	}
}
