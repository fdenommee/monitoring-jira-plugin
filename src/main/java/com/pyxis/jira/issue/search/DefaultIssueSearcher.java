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

import java.util.List;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.PagerFilter;
import com.pyxis.util.ExceptionImposter;

public class DefaultIssueSearcher
		implements IssueSearcher {

	private final SearchProvider searchProvider;
	private final JiraAuthenticationContext jiraAuthenticationContext;

	public DefaultIssueSearcher(SearchProvider searchProvider, JiraAuthenticationContext jiraAuthenticationContext) {
		this.searchProvider = searchProvider;
		this.jiraAuthenticationContext = jiraAuthenticationContext;
	}

	public List<Issue> search(SearchRequest request) {

		try {
			SearchResults results = searchProvider.search(request.getQuery(), jiraAuthenticationContext.getUser(),
														  PagerFilter.getUnlimitedFilter());

			return results.getIssues();
		}
		catch (SearchException ex) {
			throw ExceptionImposter.imposterize(ex);
		}
	}
}
