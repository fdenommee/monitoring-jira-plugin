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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public class WebDriverHelper {

	private static final int DEFAULT_TIMEOUT_IN_SECONDS = 60;

	public static WebElement findElement(WebDriver driver, By by) {
		try {
			return driver.findElement(by);
		}
		catch (NoSuchElementException ex) {
			return null;
		}
	}

	public static WebElement waitForElementToAppear(WebDriver driver, By by) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_IN_SECONDS);
		return wait.until(elementAppear(by));
	}

	public static void waitForElementToDisappear(WebDriver driver, By by) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_IN_SECONDS);
		wait.until(elementDisappear(by));
	}

	public static Function<WebDriver, WebElement> elementAppear(final By by) {
		return new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver from) {
				WebElement element = from.findElement(by);
				return isVisible(element) ? element : null;
			}
		};
	}

	public static Function<WebDriver, Boolean> elementDisappear(final By by) {
		return new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver from) {
				WebElement element = from.findElement(by);
				return !isVisible(element);
			}
		};
	}

	public static boolean isVisible(WebElement element) {
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

	@SuppressWarnings("unchecked")
	public static WebDriver newDriver() {

		Properties properties = loadPropertiesFromLocalTestResource();
		String driverClassName = properties.getProperty("driver.class", FirefoxDriver.class.getName());

		try {
			Class<WebDriver> driverClass = ClassUtils.getClass(driverClassName);

			WebDriver driver = driverClass.newInstance();

			driver.manage().setSpeed(Speed.FAST);
			driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

			return driver;
		}
		catch (Exception ex) {
			throw new RuntimeException(String.format("Cannot instanciate driver class '%s'!", driverClassName));
		}
	}

	private static Properties loadPropertiesFromLocalTestResource() {

		Properties properties = new Properties();
		InputStream is = WebDriverHelper.class.getResourceAsStream("/localtest.properties");

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
}
