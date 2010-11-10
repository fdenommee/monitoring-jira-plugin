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
package com.pyxis.jira.monitoring.rest;

import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.project.ProjectManager;
import com.pyxis.jira.issue.search.IssueSearcher;
import com.pyxis.jira.issue.search.SearchRequestBuilder;
import com.pyxis.jira.monitoring.DefaultMonitorHelper;
import com.pyxis.jira.monitoring.MonitorHelper;
import com.pyxis.jira.util.velocity.VelocityRenderer;

import static com.pyxis.jira.monitoring.IssueObjectMother.PROJECT_REST;
import static com.pyxis.jira.monitoring.IssueObjectMother.PROJECT_TEST;
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.SearchRequestObjectMother.ALL_ISSUES_FILTER_ID;
import static com.pyxis.jira.monitoring.SearchRequestObjectMother.allIssuesRequest;
import static com.pyxis.jira.monitoring.SearchRequestObjectMother.allIssuesResults;
import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static com.pyxis.jira.monitoring.UserObjectMother.VTHOULE_USER;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MonitorResourceTest {

	private MonitorHelper helper;
	private MonitorResource resource;
	@Mock private ProjectManager projectManager;
	@Mock private VelocityRenderer velocityRenderer;
	@Mock private IssueSearcher issueSearcher;
	@Mock private SearchRequestBuilder searchRequestBuilder;

	@Before
	public void init() {
		helper = new DefaultMonitorHelper(issueSearcher);
		resource = new MonitorResource(helper, velocityRenderer, projectManager, searchRequestBuilder);
	}

	@Test
	public void activeUsersByProjectAreFound() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(VTHOULE_USER, TEST_1_ISSUE);

		when(projectManager.getProjectObj(PROJECT_TEST.getId())).thenReturn(PROJECT_TEST);

		Response response = resource.getActiveUsers(MonitorResource.PROJECT_PREFIX + PROJECT_TEST.getId().toString());

		List<RestUserIssueActivity> users = toListOfUsers(response);

		assertEquals(2, users.size());
	}

	@Test
	public void activeUsersByFilterAreFound()
			throws Exception {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(VTHOULE_USER, TEST_1_ISSUE);

		SearchRequest allIssuesRequest = allIssuesRequest(ALL_ISSUES_FILTER_ID);

		when(searchRequestBuilder.forFilter(allIssuesRequest.getId())).thenReturn(allIssuesRequest);
		when(issueSearcher.search(allIssuesRequest)).thenReturn(allIssuesResults());

		Response response =
				resource.getActiveUsers(MonitorResource.FILTER_PREFIX + allIssuesRequest.getId().toString());

		List<RestUserIssueActivity> users = toListOfUsers(response);

		assertEquals(2, users.size());
	}

	@SuppressWarnings("unchecked")
	private List<RestUserIssueActivity> toListOfUsers(Response response) {
		GenericEntity<List<RestUserIssueActivity>> entities =
				(GenericEntity<List<RestUserIssueActivity>>)response.getEntity();
		return entities.getEntity();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void htmlRenderingIsSuccessfull() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(VTHOULE_USER, TEST_1_ISSUE);

		when(velocityRenderer.newVelocityParameters()).thenReturn(new HashMap<String, Object>());
		when(velocityRenderer.render(any(String.class), any(HashMap.class))).thenReturn("<html/>");

		Response response = resource.getActiveUsersHtml(PROJECT_REST.getId().toString());

		HtmlEntity htmlEntity = toHtmlEntity(response);
		assertThat(htmlEntity.isSuccess(), is(true));
	}

	@SuppressWarnings("unchecked")
	private HtmlEntity toHtmlEntity(Response response) {
		GenericEntity<HtmlEntity> entity = (GenericEntity<HtmlEntity>)response.getEntity();
		return entity.getEntity();
	}
}
