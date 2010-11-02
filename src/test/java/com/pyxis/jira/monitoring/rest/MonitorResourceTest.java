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

import com.atlassian.jira.issue.IssueManager;
import com.pyxis.jira.monitoring.DefaultMonitorHelper;
import com.pyxis.jira.monitoring.MonitorHelper;
import com.pyxis.jira.util.velocity.VelocityRenderer;

import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_MUTABLEISSUE;
import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static com.pyxis.jira.monitoring.UserObjectMother.VTHOULE_USER;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MonitorResourceTest {

	private MonitorHelper helper;
	private MonitorResource resource;
	@Mock private IssueManager issueManager;
	@Mock private VelocityRenderer velocityRenderer;

	@Before
	public void init() {
		helper = new DefaultMonitorHelper(issueManager);
		resource = new MonitorResource(issueManager, velocityRenderer, helper);
	}

	@Test
	public void activeUsersAreFound() {

		helper.notify(FDENOMMEE_USER, TEST_1_ISSUE);
		helper.notify(VTHOULE_USER, TEST_1_ISSUE);

		when(issueManager.getIssueObject(TEST_1_MUTABLEISSUE.getId())).thenReturn(TEST_1_MUTABLEISSUE);

		Response response = resource.getActiveUsers(TEST_1_ISSUE.getId());

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

		when(issueManager.getIssueObject(TEST_1_MUTABLEISSUE.getId())).thenReturn(TEST_1_MUTABLEISSUE);

		when(velocityRenderer.newVelocityParameters()).thenReturn(new HashMap<String, Object>());
		when(velocityRenderer.render(any(String.class), any(HashMap.class))).thenReturn("<html/>");

		Response response = resource.getActiveUsersHtml(TEST_1_ISSUE.getId());

		HtmlEntity htmlEntity = toHtmlEntity(response);
		assertThat(htmlEntity.isSuccess(), is(true));
	}

	@SuppressWarnings("unchecked")
	private HtmlEntity toHtmlEntity(Response response) {
		GenericEntity<HtmlEntity> entity = (GenericEntity<HtmlEntity>)response.getEntity();
		return entity.getEntity();
	}
}
