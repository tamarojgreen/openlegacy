package org.openlegacy.designtime.generators;

import org.apache.commons.lang.NotImplementedException;
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;
import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenColumnDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CodeBasedScreenTableDefinition implements ScreenTableDefinition {

	private ScreenPojoCodeModel codeModel;

	public CodeBasedScreenTableDefinition(ScreenPojoCodeModel codeModel) {
		this.codeModel = codeModel;
	}

	private static void throwNotImplemented() throws UnsupportedOperationException {
		throw (new NotImplementedException("Code based table has not implemented this method"));
	}

	public Class<?> getTableClass() {
		throwNotImplemented();
		return null;
	}

	public String getTableEntityName() {
		return codeModel.getEntityName();
	}

	public int getEndRow() {
		return codeModel.getEndRow();
	}

	public List<ScreenColumnDefinition> getColumnDefinitions() {
		Collection<Field> fields = codeModel.getFields();
		List<ScreenColumnDefinition> columnDefinitions = new ArrayList<ScreenColumnDefinition>();
		for (Field field : fields) {
			SimpleScreenColumnDefinition columnDefinition = new SimpleScreenColumnDefinition(field.getName());
			columnDefinition.setDisplayName(StringUtil.stripQuotes(field.getDisplayName()));
			columnDefinition.setStartColumn(field.getColumn());
			columnDefinition.setEndColumn(field.getEndColumn());
			columnDefinitions.add(columnDefinition);
		}
		return columnDefinitions;
	}

	public ScreenColumnDefinition getColumnDefinition(String fieldName) {
		throwNotImplemented();
		return null;
	}

	public List<String> getKeyFieldNames() {
		throwNotImplemented();
		return null;
	}

	public int getMaxRowsCount() {
		throwNotImplemented();
		return 0;
	}

	public boolean isScrollable() {
		throwNotImplemented();
		return false;
	}

	public int getStartRow() {
		return codeModel.getStartRow();
	}

	public TerminalAction getNextScreenAction() {
		throwNotImplemented();
		return null;
	}

	public TerminalAction getPreviousScreenAction() {
		throwNotImplemented();
		return null;
	}

	public DrilldownDefinition getDrilldownDefinition() {
		throwNotImplemented();
		return null;
	}

	public Class<? extends TableCollector> getTableCollector() {
		throwNotImplemented();
		return null;
	}

	public void setTableCollector(Class<? extends TableCollector> tableCollector) {
		throwNotImplemented();
	}

	public String getRowSelectionField() {
		throwNotImplemented();
		return null;
	}

	public String getMainDisplayField() {
		throwNotImplemented();
		return null;
	}

}
