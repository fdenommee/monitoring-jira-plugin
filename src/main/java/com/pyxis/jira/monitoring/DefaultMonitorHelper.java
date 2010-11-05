package com.pyxis.jira.monitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.opensymphony.user.User;

public class DefaultMonitorHelper
		implements MonitorHelper {

	private static final Logger log = Logger.getLogger(MonitorHelper.class);

	private final Map<Long, List<UserIssueActivity>> activities = new HashMap<Long, List<UserIssueActivity>>();

	public List<UserIssueActivity> getActivities(Project project) {
		List<UserIssueActivity> userActivities = getActivitiesForProject(project);
		sortByDate(userActivities);
		return userActivities;
	}

	public List<UserIssueActivity> getActivities(Issue issue) {
		List<UserIssueActivity> userActivities = getActivitiesForIssue(issue);
		sortByDate(userActivities);
		return userActivities;
	}

	public synchronized void notify(User user, Issue issue) {
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

	public synchronized void notifyDelete(Issue issue) {
		activities.remove(issue.getId());
	}

	private void sortByDate(List<UserIssueActivity> activities) {

		Collections.sort(activities, new Comparator<UserIssueActivity>() {
			public int compare(UserIssueActivity o1, UserIssueActivity o2) {
				return o2.getTime().compareTo(o1.getTime());
			}
		});
	}

	private List<UserIssueActivity> getActivitiesForProject(Project project) {

		List<UserIssueActivity> userActivities = new ArrayList<UserIssueActivity>();
		for (List<UserIssueActivity> userIssueActivities :  activities.values() ) {
			UserIssueActivity userIssueActivity = userIssueActivities.get(0);
			if (userIssueActivity.getIssue().getProjectObject().getKey().equals(project.getKey())) {
				userActivities.addAll(userIssueActivities);
			}
		}

		return userActivities;
	}

	private List<UserIssueActivity> getActivitiesForIssue(Issue issue) {

		List<UserIssueActivity> userActivities = null;
		if (issue != null) {
			userActivities = activities.get(issue.getId());
		}

		if (userActivities == null) {
			userActivities = new ArrayList<UserIssueActivity>();
		}

		return userActivities;
	}

	public void clear() {
		activities.clear();		
	}

}
