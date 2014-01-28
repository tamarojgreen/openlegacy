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
package org.openlegacy.rpc;

import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.exceptions.RpcActionException;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

public class RpcActions {

	public static class RpcActionAdapter implements RpcAction {

		public String getActionName() {
			return getClass().getSimpleName();
		}

		public void perform(RpcSession rpcSession, Object entity, Object... keys) {
			// if we got here it means the actions is not mapped...
			throw (new RpcActionNotMappedException(MessageFormat.format("Specified action {0} is not mapped to a program",
					getClass())));
		}

	}

	public static class CREATE extends RpcActionAdapter {
	}

	public static class READ extends RpcActionAdapter {
	}

	public static class UPDATE extends RpcActionAdapter {
	}

	public static class DELETE extends RpcActionAdapter {
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

	public static RpcAction newAction(String actionName) {
		Class<?>[] classes = RpcActions.class.getClasses();
		RpcAction action = null;
		for (Class<?> clazz : classes) {
			Constructor<?>[] ctors = clazz.getDeclaredConstructors();
			for (Constructor<?> ctor : ctors) {
				if (actionName.equals(clazz.getSimpleName())) {
					ctor.setAccessible(true);
					try {
						action = (RpcAction)ctor.newInstance();
					} catch (Exception e) {
						throw new RpcActionException(MessageFormat.format("Cannot instantiate an action with name {0}",
								actionName));
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
