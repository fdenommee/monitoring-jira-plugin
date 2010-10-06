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

import org.apache.log4j.Logger;

import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.pyxis.jira.monitoring.MonitorHelper;
import com.pyxis.jira.util.EventListenerUtils;

public class MonitorIssueEventListener
		extends AbstractIssueEventListener {

	private static final Logger log = Logger.getLogger(MonitorIssueEventListener.class);

	static {
		EventListenerUtils.registerListener("Monitor Issue Event Listener", MonitorIssueEventListener.class);
	}

	private final MonitorHelper helper;

	public MonitorIssueEventListener(MonitorHelper helper) {
		this.helper = helper;
	}

	public boolean isInternal() {
		return true;
	}

	@Override
	public void customEvent(IssueEvent event) {
		log.debug("custom event (" + event.getEventTypeId() + ") : " + event.toString());

		if (EventType.ISSUE_DELETED_ID.equals(event.getEventTypeId())) {
			// @todo : clear this issue from the helper list (ex: helper.cleanUp(event.getIssue())
		}
	}

	@Override
	public void workflowEvent(IssueEvent event) {
		log.debug("workflow event (" + event.getEventTypeId() + ") : " + event.toString());
	}
}
