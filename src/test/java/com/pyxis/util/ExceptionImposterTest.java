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
package com.pyxis.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionImposterTest {

	private Exception realException;

	@Test
	public void leavesUncheckedExceptionsUnchanged() {
		realException = new RuntimeException();
		assertSame(realException, ExceptionImposter.imposterize(realException));
	}

	@Test
	public void imposterizesCheckedExceptionsAndKeepsAReference() {
		realException = new Exception();
		RuntimeException imposter = ExceptionImposter.imposterize(realException);
		assertTrue(imposter instanceof ExceptionImposter);
		assertSame(realException, ((ExceptionImposter)imposter).getRealException());
	}

	@Test
	public void mimicsImposterizedExceptionToStringOutput() {
		realException = new Exception("Detail message");
		RuntimeException imposter = ExceptionImposter.imposterize(realException);
		assertEquals(realException.toString(), imposter.toString());
	}

	@Test
	public void copiesImposterizedExceptionStackTrace() {
		realException = new Exception("Detail message");
		realException.fillInStackTrace();
		RuntimeException imposter = ExceptionImposter.imposterize(realException);
		assertArrayEquals(realException.getStackTrace(), imposter.getStackTrace());
	}

	@Test
	public void mimicsImposterizedExceptionStackTraceOutput() {
		realException = new Exception("Detail message");
		realException.fillInStackTrace();
		RuntimeException imposter = ExceptionImposter.imposterize(realException);
		assertEquals(stackTraceOf(realException), stackTraceOf(imposter));
	}

	private String stackTraceOf(Exception exception) {
		StringWriter capture = new StringWriter();
		exception.printStackTrace(new PrintWriter(capture));
		capture.flush();
		return capture.toString();
	}
}