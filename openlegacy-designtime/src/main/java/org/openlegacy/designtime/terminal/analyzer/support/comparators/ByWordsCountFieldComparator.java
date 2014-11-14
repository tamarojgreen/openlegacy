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
import org.openlegacy.terminal.TerminalField;

public class ByWordsCountFieldComparator implements BestEntityNameFieldComparator {

	@Override
	public int compare(TerminalField field1, TerminalField field2) {
		int word1Count = field1.getValue().trim().split(" ").length;
		int word2Count = field2.getValue().trim().split(" ").length;

		return word2Count - word1Count;
	}
}
