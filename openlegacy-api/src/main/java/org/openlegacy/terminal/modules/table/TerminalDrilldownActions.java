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
package org.openlegacy.terminal.modules.table;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.table.TerminalDrilldownAction;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;

/**
 * A utility class for working with drill down actions. A drill down action is useful for performing drill down into a details
 * screen from a table.
 * 
 * @author Roi Mor
 * 
 */
public class TerminalDrilldownActions implements Serializable {

	private static final long serialVersionUID = 1L;

	public static TerminalDrilldownAction newAction(Class<? extends TerminalAction> action, Object actionValue) {
		SimpleDrilldownAction ddAction;
		try {
			ddAction = new SimpleDrilldownAction(action.newInstance());
			ddAction.setActionValue(actionValue);
		} catch (Exception e) {
			throw(new OpenLegacyRuntimeException(e));
		}
		return ddAction;
	}
	
	/**
	 * Use <code>TerminalDrilldownActions.newAction(ENTER.class,actionValue)</code> instead
	 * @author Roi
	 *
	 */
	@Deprecated
	public static TerminalDrilldownAction enter(Object actionValue) {
		EnterDrilldownAction action = new EnterDrilldownAction();
		action.setActionValue(actionValue);
		return action;
	}

	/**
	 * Use <code>TerminalDrilldownActions.newAction(ENTER.class,actionValue)</code> instead
	 * @author Roi
	 *
	 */
	@Deprecated
	public static class EnterDrilldownAction extends ENTER implements TerminalDrilldownAction, Serializable {

		private static final long serialVersionUID = 1L;

		private Object actionValue;

		public EnterDrilldownAction() {}

		@Override
		public Object getActionValue() {
			return actionValue;
		}

		@Override
		public void setActionValue(Object actionValue) {
			this.actionValue = actionValue;
		}
	}

	public static class SimpleDrilldownAction implements TerminalDrilldownAction, Serializable {

		private static final long serialVersionUID = 1L;

		private Object actionValue;

		private TerminalAction action;

		public SimpleDrilldownAction(TerminalAction action) {
			this.action = action;
		}

		public TerminalAction getAction() {
			return action;
		}
		
		@Override
		public Object getActionValue() {
			return actionValue;
		}

		@Override
		public void setActionValue(Object actionValue) {
			this.actionValue = actionValue;
		}

		@Override
		public void perform(TerminalSession session, Object entity,
				Object... keys) {
			action.perform(session, entity, keys);
		}

		@Override
		public boolean isMacro() {
			return action.isMacro();
		}
	}
}
