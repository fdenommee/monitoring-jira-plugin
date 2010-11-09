package com.pyxis.jira.issue;

import java.util.List;

import com.atlassian.jira.issue.Issue;

public interface IssueProvider {

	List<Issue> getIssues();
}
