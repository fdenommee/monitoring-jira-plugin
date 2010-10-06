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
package com.pyxis.jira.monitoring.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.jcip.annotations.Immutable;

@Immutable
@XmlRootElement
public class RestUserIssueActivity {

	@XmlElement
	private final String name;
	@XmlElement
	private final String issueKey;
	@XmlElement
	private final long time;

	public RestUserIssueActivity() {
		this(null, null, 0);
	}

	public RestUserIssueActivity(String name, String issueKey, long time) {
		this.name = name;
		this.issueKey = issueKey;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public long getTime() {
		return time;
	}
}
