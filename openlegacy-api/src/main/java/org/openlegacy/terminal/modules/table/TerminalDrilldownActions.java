/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.modules.table;

import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

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

	public static TerminalDrilldownAction enter(Object actionValue) {
		EnterDrilldownAction action = new EnterDrilldownAction();
		action.setActionValue(actionValue);
		return action;
	}

	public static class EnterDrilldownAction extends ENTER implements TerminalDrilldownAction, Serializable {

		private static final long serialVersionUID = 1L;

		private Object actionValue;

		public EnterDrilldownAction() {}

		public Object getActionValue() {
			return actionValue;
		}

		public void setActionValue(Object actionValue) {
			this.actionValue = actionValue;
		}
	}
}
