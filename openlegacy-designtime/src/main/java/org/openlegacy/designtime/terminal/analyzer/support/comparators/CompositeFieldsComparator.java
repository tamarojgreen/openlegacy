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
package org.openlegacy.designtime.terminal.analyzer.support.comparators;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public class CompositeFieldsComparator implements BestEntityNameFieldComparator {

	private List<BestEntityNameFieldComparator> comparators;

	public int compare(TerminalField field1, TerminalField field2) {
		for (BestEntityNameFieldComparator comparator : comparators) {
			int result = comparator.compare(field1, field2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	public void setComparators(List<BestEntityNameFieldComparator> comparators) {
		this.comparators = comparators;
	}
}