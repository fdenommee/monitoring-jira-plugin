package com.pyxis.jira.monitoring.event;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.pyxis.jira.monitoring.DefaultMonitorHelper;
import com.pyxis.jira.monitoring.MonitorHelper;
import com.pyxis.jira.monitoring.UserIssueActivity;

import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_2_ISSUE;
import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static com.pyxis.jira.monitoring.UserObjectMother.VTHOULE_USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MonitorIssueEventListenerTest {

	private MonitorHelper helper;
	private MonitorIssueEventListener eventListener;

	@Before
	public void init() {
		helper = new DefaultMonitorHelper();
		eventListener = new MonitorIssueEventListener(helper);
	}

	@Test
	public void shouldDeleteActivity() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);

		IssueEvent event = new IssueEvent(TEST_1_ISSUE, null, null, EventType.ISSUE_DELETED_ID);
		eventListener.issueDeleted(event);

		assertThat(sizeOf(activityFor(TEST_1_ISSUE)), is(0));
	}

	@Test
	public void shouldRemoveIssueActivityOnlyForDeletedIssue() {
		
		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(FDENOMMEE_USER, TEST_2_ISSUE);
		helper.notify(VTHOULE_USER, TEST_2_ISSUE);

		notifyDeletedIssue(TEST_1_ISSUE);

		assertThat(sizeOf(activityFor(TEST_1_ISSUE)), is(0));
		assertThat(sizeOf(activityFor(TEST_2_ISSUE)), is(2));
	}

	@Test
	public void shouldOnlyHandleDeleteEvent() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);

		IssueEvent event = new IssueEvent(TEST_1_ISSUE, null, null, EventType.ISSUE_UPDATED_ID);
		eventListener.issueUpdated(event);

		assertThat(sizeOf(activityFor(TEST_1_ISSUE)), is(1));
	}

	private void notifyDeletedIssue(Issue issue) {
		IssueEvent event = new IssueEvent(issue, null, null, EventType.ISSUE_DELETED_ID);
		eventListener.issueDeleted(event);
	}

	private List<UserIssueActivity> activityFor(Issue issue) {
		return helper.getActivities(issue);
	}

	private int sizeOf(List<UserIssueActivity> activities) {
		return activities.size();
	}
}
