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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
import org.openqa.selenium.Speed;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;

import it.com.pyxis.jira.LocalTestProperties;

public class WebDriverFactory {

	public static final int DEFAULT_TIMEOUT_IN_SECONDS = systemTimeout();

	private final LocalTestProperties properties;

	public WebDriverFactory(LocalTestProperties properties) {
		this.properties = properties;
	}

	public WebDriver newDriver() {
		String xvfbDisplayId = getXvfbDisplayId();
		return xvfbDisplayId == null ? newCustomDriver() : newXvfbDriver(xvfbDisplayId);
	}

	private WebDriver newXvfbDriver(String xvfbDisplayId) {

		FirefoxBinary firefox = new FirefoxBinary();
		firefox.setEnvironmentProperty("DISPLAY", xvfbDisplayId);

		WebDriver driver = new FirefoxDriver(firefox, null);

		setDriverSpeed(driver);
		setTimeout(driver);

		return driver;
	}

	@SuppressWarnings("unchecked")
	private WebDriver newCustomDriver() {

		String driverClassName = properties.driverClass(FirefoxDriver.class.getName());

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

	private String getXvfbDisplayId() {

		File displayPropertyFile = new File("target/selenium/display.properties");

		if (!displayPropertyFile.exists()) {
			return null;
		}

		Properties displayProperties = new Properties();
		FileInputStream is = null;

		try {
			is = new FileInputStream(displayPropertyFile);

			displayProperties.load(is);
		}
		catch (IOException ex) {
			// fallback to default properties
		}
		finally {
			IOUtils.closeQuietly(is);
		}

		return displayProperties.getProperty("DISPLAY", ":38");
	}

	private static int systemTimeout() {
		int timeout = Integer.parseInt(System.getProperty("timeout", "30"));
		System.err.printf("Note: Using timeout of %s\n", timeout);
		return timeout;
	}
}
