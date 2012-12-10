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
package org.openlegacy.terminal.support.wait_conditions;

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.wait_conditions.WaitCoditionAdapter;
import org.openlegacy.utils.ProxyUtil;

public class WaitForEntity extends WaitCoditionAdapter {

	private Class<? extends ScreenEntity> expectedEntityClass;

	public WaitForEntity(Class<? extends ScreenEntity> expectedEntityClass) {
		this.expectedEntityClass = expectedEntityClass;
	}

	public boolean continueWait(TerminalSession terminalSession) {

		ScreenEntity currentEntity = terminalSession.getEntity();
		return !(ProxyUtil.isClassesMatch(currentEntity.getClass(), expectedEntityClass));
	}
}
