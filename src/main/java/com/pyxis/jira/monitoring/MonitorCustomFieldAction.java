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

import java.util.List;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class MonitorCustomFieldAction
		extends JiraWebActionSupport {

	private final IssueManager issueManager;
	private final MonitorHelper monitorHelper;

	private long issueId;

	public MonitorCustomFieldAction(IssueManager issueManager, MonitorHelper monitorHelper) {
		this.issueManager = issueManager;
		this.monitorHelper = monitorHelper;
	}

	public long getIssueId() {
		return issueId;
	}

	public void setIssueId(long issueId) {
		this.issueId = issueId;
	}

	public List<UserIssueActivity> getActivities() {
		return monitorHelper.getActivities(getIssue());
	}

	@Override
	protected String doExecute()
			throws Exception {

		return SUCCESS;
	}

	protected String doClearActivity()
			throws Exception {
		monitorHelper.clear();
		return SUCCESS;
	}

	private Issue getIssue() {
		return issueManager.getIssueObject(getIssueId());
	}
}
