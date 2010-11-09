package com.pyxis.jira.issue;

import static com.pyxis.jira.monitoring.IssueObjectMother.TEST_1_ISSUE;
import static com.pyxis.jira.monitoring.SearchRequestObjectMother.ALL_ISSUES_FILTER_ID;
import static com.pyxis.jira.monitoring.SearchRequestObjectMother.allIssuesRequest;
import static com.pyxis.jira.monitoring.SearchRequestObjectMother.allIssuesResults;
import static com.pyxis.jira.monitoring.UserObjectMother.FDENOMMEE_USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;

@RunWith(MockitoJUnitRunner.class)
public class DefaultIssueProviderBuilderTest {

	private IssueProviderBuilder issueProviderBuilder;
	private static final long FILTER_ALL_ISSUES_ID = 10000;
	@Mock	private SearchRequestService searchRequestService;
	@Mock	private JiraAuthenticationContext authenticationContext;
	@Mock	private SearchProvider searchProvider;

	@Before
	public void init() {
		issueProviderBuilder = new DefaultIssueProviderBuilder(authenticationContext, searchRequestService, searchProvider);
	}

	@Test
	public void shouldReturnProvidedIssues() throws Exception {

		issueProviderBuilder.setIssues(Arrays.asList((Issue) TEST_1_ISSUE));
		IssueProvider issueProvider = issueProviderBuilder.build();

		List<Issue> actualIssues = issueProvider.getIssues();
		List<Issue> expectedIssues = Arrays.asList((Issue) TEST_1_ISSUE);

		assertThat(actualIssues.containsAll(expectedIssues), is(true));
	}

	@Test
	public void shouldReturnFilterIssues() throws Exception {

		SearchRequest allIssuesRequest = allIssuesRequest(ALL_ISSUES_FILTER_ID);
		SearchResults allIssuesResults = allIssuesResults();

		when(searchProvider.search(any(Query.class), eq(FDENOMMEE_USER), any(PagerFilter.class))).thenReturn(allIssuesResults);
		when(searchRequestService.getFilter(any(JiraServiceContext.class), eq(ALL_ISSUES_FILTER_ID))).thenReturn(allIssuesRequest);
		when(authenticationContext.getUser()).thenReturn(FDENOMMEE_USER);

		issueProviderBuilder.setFilterId(FILTER_ALL_ISSUES_ID);
		IssueProvider issueProvider = issueProviderBuilder.build();

		List<Issue> actualIssues = issueProvider.getIssues();
		List<Issue> expectedIssues = Arrays.asList((Issue) TEST_1_ISSUE);

		assertThat(actualIssues.containsAll(expectedIssues), is(true));
	}

}
