package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.springframework.util.Assert;

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

	public boolean isRowMatch(T tableRow, Object... rowKeys) {

		ScreenTableDefinition tableDefinition = screenEntitiesRegistry.getTable(tableRow.getClass());
		List<String> keyFieldNames = tableDefinition.getKeyFieldNames();

		Assert.isTrue(rowKeys.length > 0);
		Assert.isTrue(keyFieldNames.size() == rowKeys.length);

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
