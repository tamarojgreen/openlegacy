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
package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;

import java.util.Comparator;

public class TerminalPositionContainerComparator implements Comparator<TerminalPositionContainer> {

	private static TerminalPositionContainerComparator instance = new TerminalPositionContainerComparator(false);

	private boolean rightToLeft = false;

	public TerminalPositionContainerComparator(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public static TerminalPositionContainerComparator instance() {
		return instance;
	}

	@Override
	public int compare(TerminalPositionContainer o1, TerminalPositionContainer o2) {
		TerminalPosition position1 = o1.getPosition();
		TerminalPosition position2 = o2.getPosition();

		return SnapshotUtils.comparePositions(position1, position2, rightToLeft);
	}

}
