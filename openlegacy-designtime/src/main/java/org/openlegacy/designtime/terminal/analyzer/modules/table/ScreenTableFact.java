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
package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;

import java.util.List;

public class ScreenTableFact implements ScreenFact {

	private List<TableColumnFact> tableColumns;

	public ScreenTableFact(List<TableColumnFact> tableColumns) {
		this.tableColumns = tableColumns;
	}

	public List<TableColumnFact> getTableColumns() {
		return tableColumns;
	}
}
