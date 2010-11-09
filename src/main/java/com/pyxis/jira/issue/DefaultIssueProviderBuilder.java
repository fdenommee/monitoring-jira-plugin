package com.pyxis.jira.issue;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.PagerFilter;
import com.opensymphony.user.User;

public class DefaultIssueProviderBuilder implements IssueProviderBuilder {

	private SearchRequestService searchRequestService;
	private SearchProvider searchProvider;
	private JiraAuthenticationContext authenticationContext;

	private Long filterId;
	private Long projectId;
	private List<Issue> issues = null;

	public DefaultIssueProviderBuilder(JiraAuthenticationContext authenticationContext, SearchRequestService searchRequestService, SearchProvider searchProvider) {
		super();
		this.authenticationContext = authenticationContext;
		this.searchRequestService = searchRequestService;
		this.searchProvider = searchProvider;
		
	}

	public IssueProviderBuilder setFilterId(Long id) {
		clear();
		filterId = id;
		return this;
	}

	public IssueProviderBuilder setProjectId(Long id) {
		clear();
		// TODO Auto-generated method stub
		return null;
	}

	public IssueProviderBuilder setIssues(List<Issue> issues) {
		clear();
		this.issues.addAll(issues);
		return this;
	}

	public IssueProvider build() {
		IssueProvider issueProvider = null;
		if (filterId != null) {
			SearchRequest searchRequest = getSearchRequestService().getFilter(new JiraServiceContextImpl(authenticationContext.getUser()),
					filterId);
			final SearchResults srs = getSearchResults(searchRequest, authenticationContext.getUser());
			issueProvider = new IssueProvider() {
				public List<Issue> getIssues() {
					return srs.getIssues();
				}
			};
		} else if (projectId != null) {

		} else {
			issueProvider = new IssueProvider() {
				public List<Issue> getIssues() {
					return issues;
				}
			};
		}
		return issueProvider;
	}

	private void clear() {
		filterId = null;
		projectId = null;
		issues = new ArrayList<Issue>();
	}

	private SearchRequestService getSearchRequestService() {
		return searchRequestService;
		// return (SearchRequestService)ComponentManager.getComponentInstanceOfType(SearchRequestService.class);
	}

	private SearchProvider getSearchProvider() {
		return searchProvider;
		// return (SearchProvider)ComponentManager.getComponentInstanceOfType(SearchProvider.class);
	}

	protected SearchResults getSearchResults(final SearchRequest searchRequest, final User user) {
		if (searchRequest == null) {
			return null;
		}
		try {
			return getSearchProvider().search(searchRequest.getQuery(), user, PagerFilter.getUnlimitedFilter());
		} catch (SearchException e) {
			return null;
		}
	}

}
