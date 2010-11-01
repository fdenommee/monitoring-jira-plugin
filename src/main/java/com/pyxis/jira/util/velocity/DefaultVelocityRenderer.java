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
package com.pyxis.jira.util.velocity;

import java.util.Map;

import org.apache.velocity.exception.VelocityException;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.JiraVelocityUtils;
import com.atlassian.velocity.VelocityManager;

public class DefaultVelocityRenderer
		implements VelocityRenderer {

	private final VelocityManager velocityManager;
	private final JiraAuthenticationContext authenticationContext;

	public DefaultVelocityRenderer(VelocityManager velocityManager, JiraAuthenticationContext authenticationContext) {
		this.velocityManager = velocityManager;
		this.authenticationContext = authenticationContext;
	}

	public String render(String template, Map<String, Object> parameters) {

		try {
			return velocityManager.getEncodedBody("", template, "UTF8", parameters);
		}
		catch (VelocityException e) {
			return e.toString();
		}
	}

	public Map<String, Object> newVelocityParameters() {
		Map<String, Object> parameters = JiraVelocityUtils.createVelocityParams(authenticationContext);
		parameters.put("i18n", authenticationContext.getI18nHelper());
		return parameters;
	}
}