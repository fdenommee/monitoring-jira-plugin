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

/**
 * @author http://noncomplexstuff.com/2010/05/25/exception-imposter.html
 */
public class ExceptionImposter
		extends RuntimeException {

	private final Exception imposterized;

	public static RuntimeException imposterize(Exception e) {
		if (e instanceof RuntimeException) return (RuntimeException)e;

		return new ExceptionImposter(e);
	}

	public ExceptionImposter(Exception e) {
		super(e.getMessage(), e.getCause());
		imposterized = e;
		setStackTrace(e.getStackTrace());
	}

	public Exception getRealException() {
		return imposterized;
	}

	public String toString() {
		return imposterized.toString();
	}
}