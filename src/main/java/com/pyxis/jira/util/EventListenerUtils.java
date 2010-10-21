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
package com.pyxis.jira.util;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.ofbiz.core.entity.GenericValue;

import com.atlassian.core.ofbiz.CoreFactory;
import com.atlassian.jira.action.admin.ListenerCreate;

public class EventListenerUtils {

	private static final Logger log = Logger.getLogger(EventListenerUtils.class);

	@SuppressWarnings("unchecked")
	public static void registerListener(String name, Class clazz) {

		try {
			Collection<GenericValue> actualListeners = CoreFactory.getGenericDelegator().findAll("ListenerConfig");

			for (GenericValue listener : actualListeners) {
				if (listener.getString("name").equals(name)) {
					log.debug("Listener '" + name + "/" + clazz.getName() + "' already registered");
					return;
				}
			}

			createListener(name, clazz);
		}
		catch (Exception ex) {
			log.error("Registering listener '" + name + "/" + clazz.getName() + "' fail", ex);
			throw new /*?*/RuntimeException("Cannot register listener '" + name + "/" + clazz.getName() + "'", ex);
		}
	}

	private static void createListener(String name, Class clazz)
			throws Exception {

		ListenerCreate listener = new ListenerCreate();
		listener.setName(name);
		listener.setClazz(clazz.getName());
		listener.execute();
	}
}