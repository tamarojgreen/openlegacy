/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.db.actions;

import org.openlegacy.db.DbSession;
import org.openlegacy.db.exceptions.DbActionException;
import org.openlegacy.db.exceptions.DbActionNotMappedException;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

public class DbActions {

	public static class DbActionAdapter implements DbAction {

		public String getActionName() {
			return getClass().getSimpleName();
		}

		public void perform(DbSession session, Object entity, Object... keys) {
			throw new DbActionNotMappedException(MessageFormat.format("Specified action {0} is not mapped to a DB session",
					getClass()));
		}
	}

	public static class CREATE extends DbActionAdapter {
	}

	public static class READ extends DbActionAdapter {
	}

	public static class UPDATE extends DbActionAdapter {
	}

	public static class DELETE extends DbActionAdapter {
	}

	public static CREATE CREATE() {
		return new CREATE();
	}

	public static READ READ() {
		return new READ();
	}

	public static UPDATE UPDATE() {
		return new UPDATE();
	}

	public static DELETE DELETE() {
		return new DELETE();
	}

	public static DbAction newAction(String actionName) {
		Class<?>[] classes = DbActions.class.getClasses();
		DbAction action = null;
		for (Class<?> clazz : classes) {
			Constructor<?>[] ctors = clazz.getDeclaredConstructors();
			for (Constructor<?> ctor : ctors) {
				if (actionName.equals(clazz.getSimpleName())) {
					ctor.setAccessible(true);
					try {
						action = (DbAction)ctor.newInstance();
					} catch (Exception e) {
						throw new DbActionException(
								MessageFormat.format("Cannot instantiate an action with name {0}", actionName));
					}
					break;
				}
			}
			if (action != null) {
				break;
			}
		}
		return action;
	}
}
