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
package org.openlegacy.providers.applinx;

import com.sabratec.applinx.common.runtime.GXScreenPosition;
import com.sabratec.util.GXPosition;

import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

public class ApxPositionUtil {

	public static TerminalPosition toTerminalPosition(GXPosition position) {
		return new SimpleTerminalPosition(position.getRow(), position.getColumn());
	}

	public static TerminalPosition toScreenPosition(GXScreenPosition position) {
		return new SimpleTerminalPosition(position.getRow(), position.getColumn());
	}

	public static GXPosition toPosition(TerminalPosition position) {
		return new GXPosition(position.getRow(), position.getColumn());
	}
}
