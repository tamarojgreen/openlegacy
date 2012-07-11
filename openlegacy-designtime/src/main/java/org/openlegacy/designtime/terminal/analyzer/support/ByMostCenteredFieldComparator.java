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
package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;

public class ByMostCenteredFieldComparator implements BestEntityNameFieldComparator {

	public int compare(TerminalField field1, TerminalField field2) {

		int field1DistanceFromCenter = Math.abs((ScreenSize.DEFAULT_COLUMN / 2) - field1.getPosition().getColumn());
		int field2DistanceFromCenter = Math.abs((ScreenSize.DEFAULT_COLUMN / 2) - field2.getPosition().getColumn());

		// the field who is distance is smallest should be 1st
		return field1DistanceFromCenter - field2DistanceFromCenter;
	}
}
