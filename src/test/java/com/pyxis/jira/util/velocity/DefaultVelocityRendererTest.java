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

import java.util.Collections;

import org.apache.velocity.exception.VelocityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.velocity.VelocityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultVelocityRendererTest {

	private DefaultVelocityRenderer velocityRenderer;
	@Mock private VelocityManager velocityManager;

	@Before
	public void init() {
		velocityRenderer = new DefaultVelocityRenderer(velocityManager, null);
	}

	@Test
	public void shouldRenderTheExceptionStacktrace()
			throws Exception {

		when(velocityManager.getEncodedBody("", "", "UTF8", Collections.<String, Object>emptyMap()))
				.thenThrow(new VelocityException("The Exception StackTrace"));

		assertThat(velocityRenderer.render("", Collections.<String, Object>emptyMap()),
				   is("org.apache.velocity.exception.VelocityException: The Exception StackTrace"));
	}

	@Test
	public void shouldRenderSuccessfully()
			throws Exception {

		when(velocityManager.getEncodedBody("", "", "UTF8", Collections.<String, Object>emptyMap()))
				.thenReturn("<html/>");

		assertThat(velocityRenderer.render("", Collections.<String, Object>emptyMap()), is("<html/>"));
	}
}
