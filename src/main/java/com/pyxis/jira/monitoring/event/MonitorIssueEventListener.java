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
package com.pyxis.jira.monitoring.event;

import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.pyxis.jira.monitoring.MonitorHelper;

public class MonitorIssueEventListener
		extends AbstractIssueEventListener {

	private final MonitorHelper monitorHelper;

	public MonitorIssueEventListener(MonitorHelper monitorHelper) {
		this.monitorHelper = monitorHelper;
	}

	public void issueDeleted(IssueEvent event) {
		cleanActivitiesForIssue(event.getIssue());
	}

	public void customEvent(IssueEvent event) {
		if (EventType.ISSUE_DELETED_ID.equals(event.getEventTypeId())) {
			cleanActivitiesForIssue(event.getIssue());
		}
	}

	private void cleanActivitiesForIssue(Issue issue) {
		getMonitorHelper().notifyDelete(issue);
	}

	private MonitorHelper getMonitorHelper() {
		return monitorHelper;
	}
}