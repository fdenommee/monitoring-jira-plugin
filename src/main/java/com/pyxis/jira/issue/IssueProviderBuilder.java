package com.pyxis.jira.issue;

import java.util.List;

import com.atlassian.jira.issue.Issue;

public interface IssueProviderBuilder {

	IssueProviderBuilder setFilterId(Long id);

	IssueProviderBuilder setProjectId(Long id);

	IssueProviderBuilder setIssues(List<Issue> issues);

	IssueProvider build();
}
