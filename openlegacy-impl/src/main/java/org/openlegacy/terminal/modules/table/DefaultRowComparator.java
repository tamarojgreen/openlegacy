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
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Default terminal row comparator implementation. Fetch the given table row definition and it's key fields. If the key fields
 * values matches the given row POJO field values then a match is declared. False otherwise
 * 
 */
public class DefaultRowComparator<T> implements RowComparator<T> {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Override
	public boolean isRowMatch(T tableRow, Object... rowKeys) {

		ScreenTableDefinition tableDefinition = screenEntitiesRegistry.getTable(tableRow.getClass());
		List<String> keyFieldNames = tableDefinition.getKeyFieldNames();

		Assert.isTrue(rowKeys.length > 0, "No table keys is defined for " + tableRow.getClass().getName());
		Assert.isTrue(keyFieldNames.size() == rowKeys.length, MessageFormat.format(
				"Table {0} keys count ({1}) doesnt match provided keys ({2}) count", tableRow.getClass().getName(),
				keyFieldNames.size(), Arrays.toString(rowKeys)));

		ScreenPojoFieldAccessor rowFieldsAccessor = new SimpleScreenPojoFieldAccessor(tableRow);

		int keyCount = 0;
		for (String keyFieldName : keyFieldNames) {
			String currentCellValue = rowFieldsAccessor.getFieldValue(keyFieldName).toString();
			String expectedCellValue = rowKeys[keyCount].toString();
			if (!currentCellValue.equals(expectedCellValue)) {
				return false;
			}
			keyCount++;
		}

		return true;
	}
}
