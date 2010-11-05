package com.pyxis.jira.monitoring.event;

import com.pyxis.jira.util.EventListenerUtils;

public class MonitorIssueEventRegistror {

	static {
		EventListenerUtils.registerListener("Monitor Issue Event Listener", MonitorIssueEventListener.class);
	}

}
