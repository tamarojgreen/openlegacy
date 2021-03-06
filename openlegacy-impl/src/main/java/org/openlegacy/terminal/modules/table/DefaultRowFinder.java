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
package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;

import java.util.List;

/**
 * Default implementation of row finder. Scans the list of rows and ask row comparator whether a given row is a match
 * 
 */
public class DefaultRowFinder<T> implements RowFinder<T> {

	@Override
	public Integer findRow(RowComparator<T> rowComparator, List<T> tableRows, Object... rowKeys) {
		int rowCount = 0;

		for (T tableRow : tableRows) {
			if (rowComparator.isRowMatch(tableRow, rowKeys)) {
				return rowCount;
			}
			rowCount++;
		}
		return null;
	}
}
