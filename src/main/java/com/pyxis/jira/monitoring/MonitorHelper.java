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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.atlassian.jira.issue.Issue;
import com.opensymphony.user.User;

public class MonitorHelper {

	private static final Logger log = Logger.getLogger(MonitorHelper.class);

	private final List<UserIssueActivity> activities = new ArrayList<UserIssueActivity>();

	public List<UserIssueActivity> getActivities() {
		return activities;
	}

	public void notify(User user, Issue issue) {
		log.info("notify : user = " + user.getName() + " / " + issue.getKey() + "( " + Thread.currentThread().toString() + ")");

		UserIssueActivity activity = new UserIssueActivity(user.getName(), issue);

		if (activities.contains(activity)) {
			activities.remove(activity);
		}

		activities.add(activity);
	}
}
