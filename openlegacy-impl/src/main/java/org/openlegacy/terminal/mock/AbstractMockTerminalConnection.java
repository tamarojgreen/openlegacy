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
package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.TerminalConnection;

import java.io.Serializable;

public abstract class AbstractMockTerminalConnection implements TerminalConnection, Serializable {

	private static final long serialVersionUID = 1L;

	private boolean rightToLeftState;
	@Override
	public void flip() {
		//throw (new UnsupportedOperationException("Not implemented flip for mockup session"));
		rightToLeftState = !rightToLeftState;
	}

	@Override
	public boolean isRightToLeftState() {
		return rightToLeftState;
	}
}
