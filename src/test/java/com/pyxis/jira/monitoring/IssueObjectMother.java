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

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.project.Project;

import static org.mockito.Mockito.*;

public class IssueObjectMother {

	public static final Project PROJECT_REST = newProject("REST", 10010);
	
	public static final Project PROJECT_TEST = newProject("TEST", 10000);

	public static final MutableIssue TEST_1_ISSUE = newMutableIssue("TEST-1", 1001, PROJECT_TEST);
	public static final MutableIssue TEST_2_ISSUE = newMutableIssue("TEST-2", 1002, PROJECT_TEST);
	
	public static final Project PROJECT_OTHER_TEST = newProject("OTH", 10010);

	public static final MutableIssue OTHER_TEST_1_ISSUE = newMutableIssue("OTH-1", 2001, PROJECT_OTHER_TEST);
	public static final MutableIssue OTHER_TEST_2_ISSUE = newMutableIssue("OTH-2", 2002, PROJECT_OTHER_TEST);

	public static final MutableIssue UNKNOWN_ISSUE = newMutableIssue("TEST-99999", -1, null);

	public static final MutableIssue TEST_1_MUTABLEISSUE = newMutableIssue("TEST-1", 1001, PROJECT_TEST);

	public static Issue newIssue(String key, long id) {
		Issue issue = mock(Issue.class);
		when(issue.getKey()).thenReturn(key);
		when(issue.getId()).thenReturn(id);
		return issue;
	}

	public static MutableIssue newMutableIssue(String key, long id, Project project) {
		MutableIssue issue = mock(MutableIssue.class);
		when(issue.getKey()).thenReturn(key);
		when(issue.getId()).thenReturn(id);
		when(issue.getProjectObject()).thenReturn(project);		
		return issue;
	}

	public static Project newProject(String key, long id) {
		Project project = mock(Project.class);
		when(project.getKey()).thenReturn(key);
		when(project.getId()).thenReturn(id);
		return project;
	}
	
}
