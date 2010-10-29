package com.pyxis.jira.monitoring;

import com.pyxis.jira.monitoring.event.MonitorIssueEventListener;
import com.pyxis.jira.util.EventListenerUtils;

public class MonitorEventHelper {

	public MonitorEventHelper() {
		EventListenerUtils.registerListener("Monitor Issue Event Listener", MonitorIssueEventListener.class);
	}

}
