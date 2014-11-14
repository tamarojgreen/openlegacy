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
package org.openlegacy.designtime.terminal.analyzer.support.comparators;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;

public class ByMostCenteredFieldComparator implements BestEntityNameFieldComparator {

	@Override
	public int compare(TerminalField field1, TerminalField field2) {

		int column1 = field1.getPosition().getColumn();
		int column2 = field2.getPosition().getColumn();
		int field1DistanceFromCenter = Math.abs((ScreenSize.DEFAULT_COLUMN / 2) - column1) + (field1.getLength() / 2);
		int field2DistanceFromCenter = Math.abs((ScreenSize.DEFAULT_COLUMN / 2) - column2) + (field2.getLength() / 2);

		// the field who is distance is smallest should be 1st
		return field1DistanceFromCenter - field2DistanceFromCenter;
	}
}
