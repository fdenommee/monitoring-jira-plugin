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

import java.util.Arrays;
import java.util.List;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.issue.search.SearchResults;

import static com.pyxis.jira.monitoring.IssueObjectMother.OTHER_TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.IssueObjectMother.OTHER_TEST_2_ISSUE;
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_2_ISSUE;
import static org.mockito.Mockito.*;

public class SearchRequestObjectMother {

	public static final Long ALL_ISSUES_FILTER_ID = (long)10000;

	public static SearchRequest newSearchRequest(String name, long id) {
		SearchRequest searchRequest = mock(SearchRequest.class);
		when(searchRequest.getName()).thenReturn(name);
		when(searchRequest.getId()).thenReturn(id);
		return searchRequest;
	}
	
	public static SearchResults newSearchResults(List<Issue> issues) {
		SearchResults searchResults = mock(SearchResults.class);
		when(searchResults.getIssues()).thenReturn(issues);
		return searchResults;
	}
	
	public static SearchResults allIssuesResults() {
		return newSearchResults(Arrays.asList((Issue)TEST_1_ISSUE,(Issue)TEST_2_ISSUE,(Issue)OTHER_TEST_1_ISSUE,(Issue)OTHER_TEST_2_ISSUE));
	}
	
	public static SearchRequest allIssuesRequest(long filterId) {
		return newSearchRequest("All Issues", filterId);
	}
}
