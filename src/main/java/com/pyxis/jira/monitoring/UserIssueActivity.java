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
package com.pyxis.jira.monitoring;

import java.util.Date;

import org.apache.commons.lang.ObjectUtils;

import com.atlassian.jira.issue.Issue;

public class UserIssueActivity {

	private String userName;
	private Issue issue;
	private Date time;

	public UserIssueActivity(String userName, Issue issue) {
		this.userName = userName;
		this.issue = issue;
		this.time = new Date(System.currentTimeMillis());
	}

	public String getUserName() {
		return userName;
	}

	public Issue getIssue() {
		return issue;
	}

	public Date getTime() {
		return time;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof UserIssueActivity)) {
			return false;
		}

		UserIssueActivity rhs = (UserIssueActivity)obj;

		return ObjectUtils.equals(getUserName(), rhs.getUserName())
			   && ObjectUtils.equals(getIssue().getKey(), rhs.getIssue().getKey());
	}
}
