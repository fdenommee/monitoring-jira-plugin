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
package it.com.pyxis.jira;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.atlassian.jira.functest.framework.Administration;
import com.atlassian.jira.functest.framework.FuncTestCase;
import com.atlassian.jira.functest.framework.Navigation;
import com.atlassian.jira.functest.framework.assertions.Assertions;

public abstract class JiraFuncTestCase {

	private static FuncTestCaseWrapper wrapper;

	public static final String ADMIN = "admin";

	public static Administration administration;
	public static Navigation navigation;
	public static Assertions assertions;

	@BeforeClass
	public static void init() {
		wrapper = new FuncTestCaseWrapper();
		wrapper.doSetup();

		administration = wrapper.administration();
		navigation = wrapper.navigation();
		assertions = wrapper.assertions();
	}

	@AfterClass
	public static void destroy() {
		wrapper.doTearDown();
		wrapper = null;
		administration = null;
		navigation = null;
		assertions = null;
	}

	private static class FuncTestCaseWrapper
			extends FuncTestCase {

		Administration administration() {
			return administration;
		}

		Navigation navigation() {
			return navigation;
		}

		Assertions assertions() {
			return assertions;
		}

		void doSetup() {
			setUp();
		}

		void doTearDown() {
			tearDown();
		}
	}
}