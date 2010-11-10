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

import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.security.JiraAuthenticationContext;

public class DefaultSearchRequestBuilder
		implements SearchRequestBuilder {

	private final JiraAuthenticationContext jiraAuthenticationContext;
	private final SearchRequestService searchRequestService;

	public DefaultSearchRequestBuilder(SearchRequestService searchRequestService,
									   JiraAuthenticationContext jiraAuthenticationContext) {
		this.searchRequestService = searchRequestService;
		this.jiraAuthenticationContext = jiraAuthenticationContext;
	}

	public SearchRequest forFilter(long filterId) {
		return searchRequestService.getFilter(buildContext(), filterId);
	}

	private JiraServiceContextImpl buildContext() {
		return new JiraServiceContextImpl(jiraAuthenticationContext.getUser());
	}
}
