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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.atlassian.jira.issue.Issue;
import com.opensymphony.user.User;

public class MonitorHelper {

	private static final Logger log = Logger.getLogger(MonitorHelper.class);

	private final Map<Long, List<UserIssueActivity>> activities = new HashMap<Long, List<UserIssueActivity>>();

	public List<UserIssueActivity> getActivities(Issue issue) {
		return getActivitiesSortedByDate(issue);
	}

	public void notify(User user, Issue issue) {
		log.info("notify : user = " + user.getName() + " / " + issue.getId() + "( " +
				 Thread.currentThread().toString() + ")");
		
		UserIssueActivity activity = new UserIssueActivity(user.getName(), issue);

		List<UserIssueActivity> userActivities = getActivitiesForIssue(issue);

		if (userActivities.contains(activity)) {
			userActivities.remove(activity);
		}
		
		userActivities.add(activity);
		activities.put(issue.getId(), userActivities);
	}

	private List<UserIssueActivity> getActivitiesSortedByDate(Issue issue) {

		List<UserIssueActivity> sortedActivities = getActivitiesForIssue(issue);

		Collections.sort(sortedActivities, new Comparator<UserIssueActivity>() {
			public int compare(UserIssueActivity o1, UserIssueActivity o2) {
				return o2.getTime().compareTo(o1.getTime());
			}
		});

		return sortedActivities;
	}
	
	private List<UserIssueActivity> getActivitiesForIssue(Issue issue) {
		
		List<UserIssueActivity> userActivities = activities.get(issue.getId());
		
		if (userActivities == null) {
			userActivities = new ArrayList<UserIssueActivity>();
		}
		
		return userActivities;
	}
}
