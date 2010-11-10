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

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;
import com.opensymphony.user.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultIssueSearcherTest {

	@Mock private Query query;
	@Mock private SearchProvider searchProvider;
	@Mock private JiraAuthenticationContext jiraAuthenticationContext;

	private DefaultIssueSearcher issueSearcher;

	@Before
	public void init() {
		issueSearcher = new DefaultIssueSearcher(searchProvider, jiraAuthenticationContext);
	}

	@Test
	public void noIssueFound()
			throws Exception {

		List<Issue> expectedIssues = Arrays.asList();

		when(searchProvider.search(any(Query.class), any(User.class), any(PagerFilter.class)))
				.thenReturn(new SearchResults(expectedIssues, PagerFilter.getUnlimitedFilter()));

		List<Issue> actualIssues = issueSearcher.search(new SearchRequest(query));

		assertThat(actualIssues.size(), is(equalTo(0)));
	}

	@Test(expected = RuntimeException.class)
	public void verifyExceptionPropagation()
			throws Exception {

		when(searchProvider.search(any(Query.class), any(User.class), any(PagerFilter.class))).
				thenThrow(new SearchException("a message"));

		issueSearcher.search(new SearchRequest(query));
	}
}
