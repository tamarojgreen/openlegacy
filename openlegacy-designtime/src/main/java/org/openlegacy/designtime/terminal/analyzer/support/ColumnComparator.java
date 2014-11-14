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
package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.analyzer.modules.table.TableColumnFact;

import java.util.Comparator;

public class ColumnComparator implements Comparator<TableColumnFact> {

	private static ColumnComparator instance = new ColumnComparator();

	public static ColumnComparator instance() {
		return instance;
	}

	@Override
	public int compare(TableColumnFact o1, TableColumnFact o2) {
		return o1.getFields().get(0).getPosition().getColumn() - o2.getFields().get(0).getPosition().getColumn();
	}

}
