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
package com.pyxis.jira.issue.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.security.JiraAuthenticationContext;

import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSearchRequestBuilderTest {

	@Mock private JiraAuthenticationContext jiraAuthenticationContext;
	@Mock private SearchRequestService searchRequestService;

	private DefaultSearchRequestBuilder requestBuilder;

	@Before
	public void init() {
		when(jiraAuthenticationContext.getUser()).thenReturn(FDENOMMEE_USER);

		requestBuilder = new DefaultSearchRequestBuilder(searchRequestService, jiraAuthenticationContext);
	}

	@Test
	public void canBuildARequestForAFilter() {

		when(searchRequestService.getFilter(any(JiraServiceContext.class), eq(10000L)))
				.thenReturn(new SearchRequest());

		SearchRequest request = requestBuilder.forFilter(10000L);

		assertThat(request, is(notNullValue()));
	}
}
