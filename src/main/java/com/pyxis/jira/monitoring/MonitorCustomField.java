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

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.security.JiraAuthenticationContext;

public class MonitorCustomField
		extends CalculatedCFType {

	private final MonitorHelper monitorHelper;
	private final JiraAuthenticationContext authenticationContext;

	public MonitorCustomField(MonitorHelper monitorHelper, JiraAuthenticationContext authenticationContext) {
		this.monitorHelper = monitorHelper;
		this.authenticationContext = authenticationContext;
	}

	public String getStringFromSingularObject(Object o) {
		return ObjectUtils.toString(o);
	}

	public Object getSingularObjectFromString(String s)
			throws FieldValidationException {
		return s;
	}

	@SuppressWarnings("unchecked")
	public Object getValueFromIssue(CustomField field, Issue issue) {
		// @todo : find another hook for notification - this method is call very often
		monitorHelper.notify(authenticationContext.getUser(), issue);
		//this method is call very often, and we need to sort the activities (which is done for the view)
		return Collections.<UserIssueActivity>emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getVelocityParameters(Issue issue, CustomField field, FieldLayoutItem fieldLayoutItem) {
		Map<String, Object> map = super.getVelocityParameters(issue, field, fieldLayoutItem);
		map.put("activities", monitorHelper.getActivities(issue));
		map.put("outlookdate", authenticationContext.getOutlookDate());
		return map;
	}
}
