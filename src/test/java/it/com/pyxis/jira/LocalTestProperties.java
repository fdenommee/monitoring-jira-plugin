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
package it.com.pyxis.jira;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class LocalTestProperties
		extends Properties {

	public LocalTestProperties() {
		super();
		loadProperties();
	}

	public String driverClass(String defaultValue) {
		return getProperty("driver.class", defaultValue);
	}

	public String hostUrl() {
		StringBuilder url = new StringBuilder();

		url.append(getProperty("jira.protocol", "http"))
				.append("://").append(getProperty("jira.host", "localhost"))
				.append(":").append(getProperty("jira.port", "2990"));

		return url.toString();
	}

	public String homeUrl() {
		return hostUrl() + getProperty("jira.context");
	}

	private void loadProperties() {

		InputStream is = LocalTestProperties.class.getResourceAsStream("/localtest.properties");

		try {
			load(is);
		}
		catch (IOException ex) {
			// fallback to default properties
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}
}
