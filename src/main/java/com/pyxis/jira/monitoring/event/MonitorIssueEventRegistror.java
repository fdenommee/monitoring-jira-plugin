package com.pyxis.jira.monitoring.event;

import org.apache.log4j.Logger;

import com.pyxis.jira.monitoring.event.MonitorIssueEventListener;
import com.pyxis.jira.util.EventListenerUtils;

public class MonitorIssueEventRegistror {

	private static final Logger log = Logger.getLogger(MonitorIssueEventListener.class);

	static {
		log.fatal("Registering listener...");
		EventListenerUtils.registerListener("Monitor Issue Event Listener", MonitorIssueEventListener.class);
	}

}
