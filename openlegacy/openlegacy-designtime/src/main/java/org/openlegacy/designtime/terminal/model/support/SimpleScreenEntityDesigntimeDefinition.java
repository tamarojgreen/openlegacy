package org.openlegacy.designtime.terminal.model.support;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalRectangle;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SimpleScreenEntityDesigntimeDefinition extends SimpleScreenEntityDefinition implements ScreenEntityDesigntimeDefinition, Serializable {

	private static final String RECORD = "Record";

	public SimpleScreenEntityDesigntimeDefinition() {
		super(null, null);
	}

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;

	private String packageName;

	private TerminalRectangle snapshotBorders = null;

	private List<SimpleTableDefinition> temporaryTableDefinitions = new ArrayList<SimpleTableDefinition>();

	private List<String> imports = new ArrayList<String>();

	@Override
	public void setEntityName(String entityName) {
		super.setEntityName(entityName);
		populateTableNames(entityName);
	}

	/**
	 * Get names to table. Table rely on the screen entity name, and one screen entity name is set, also set names for tables
	 */
	private void populateTableNames(String entityName) {
		int count = 0;
		for (SimpleTableDefinition tableDefinition : temporaryTableDefinitions) {

			String tableSuffix = count == 0 ? "" : String.valueOf(count - 1);
			String tableEntityName = MessageFormat.format("{0}{1}{2}", entityName, RECORD, tableSuffix);
			tableDefinition.setTableEntityName(tableEntityName);
			getTableDefinitions().put(tableEntityName, tableDefinition);
		}
		temporaryTableDefinitions.clear();
	}

	@Override
	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
	}

	@Override
	public TerminalSnapshot getSnapshot() {
		return terminalSnapshot;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public TerminalRectangle getSnapshotBorders() {
		if (snapshotBorders == null) {
			TerminalPosition topLeft = new SimpleTerminalPosition(1, 1);
			ScreenSize size = getSnapshot().getSize();
			TerminalPosition buttomRight = new SimpleTerminalPosition(size.getRows(), size.getColumns());
			snapshotBorders = new SimpleTerminalRectangle(topLeft, buttomRight);
		}
		return snapshotBorders;
	}

	public void setSnapshotBorders(TerminalRectangle snapshotBorders) {
		this.snapshotBorders = snapshotBorders;
		setWindow(true);
	}

	public void addTemporaryTable(SimpleTableDefinition tableDefinition) {
		temporaryTableDefinitions.add(tableDefinition);
	}

	public List<ScreenFieldDefinition> getSortedFields() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();

		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
		return sortedFields;
	}

	@Override
	public String toString() {
		return getSnapshot().toString();
	}

	public List<String> getReferredClasses() {
		return imports;
	}

}
