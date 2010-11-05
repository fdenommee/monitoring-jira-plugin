package com.pyxis.jira.monitoring.event;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.IssueManager;
import com.opensymphony.user.User;
import com.pyxis.jira.monitoring.DefaultMonitorHelper;
import com.pyxis.jira.monitoring.MonitorHelper;

import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_2_ISSUE;
import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static com.pyxis.jira.monitoring.UserObjectMother.VTHOULE_USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MonitorIssueEventListenerTest {

	private MonitorHelper helper;
	private MonitorIssueEventListener eventListener;
	@Mock private IssueManager issueManager;

	@Before
	public void init() {
		helper = new DefaultMonitorHelper();
		eventListener = new MonitorIssueEventListener(helper);
	}

	@Test
	public void shouldDoNothingOnOtherEvent() {
		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		
		IssueEvent event = new IssueEvent(TEST_1_ISSUE, (Map)null, (User)null, EventType.ISSUE_UPDATED_ID);
		eventListener.customEvent(event);
		
		assertThat(helper.getActivities(TEST_1_ISSUE).size(), is(equalTo(1)));
		
	}

	@Test
	public void shouldRemoveIssueActivityForDeletedIssue() {
		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		
		IssueEvent event = new IssueEvent(TEST_1_ISSUE, (Map)null, (User)null, EventType.ISSUE_DELETED_ID);
		eventListener.issueDeleted(event);
		
		assertThat(helper.getActivities(TEST_1_ISSUE).size(), is(equalTo(0)));
		
	}

	@Test
	public void shouldRemoveIssueActivityOnlyForDeletedIssue() {
		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		
		helper.notify(FDENOMMEE_USER, TEST_2_ISSUE);
		helper.notify(VTHOULE_USER, TEST_2_ISSUE);
	
		IssueEvent event = new IssueEvent(TEST_1_ISSUE, (Map)null, (User)null, EventType.ISSUE_DELETED_ID);
		eventListener.issueDeleted(event);

		assertThat(helper.getActivities(TEST_1_ISSUE).size(), is(equalTo(0)));
		assertThat(helper.getActivities(TEST_2_ISSUE).size(), is(equalTo(2)));
	}
}
