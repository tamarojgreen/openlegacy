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
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.utils.StringUtil;

public class ByCharsFrequencyFieldComparator implements BestEntityNameFieldComparator {

	public int compare(TerminalField field1, TerminalField field2) {
		int field1CharsFrquency = StringUtil.getDifferentCharsCount(field1.getValue());
		int field2CharsFrquency = StringUtil.getDifferentCharsCount(field2.getValue());

		return field2CharsFrquency - field1CharsFrquency;
	}
}
