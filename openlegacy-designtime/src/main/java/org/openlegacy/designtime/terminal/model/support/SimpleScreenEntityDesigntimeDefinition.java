package org.openlegacy.designtime.terminal.model.support;

import org.apache.commons.lang.SerializationUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenTableDefinition;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalRectangle;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SimpleScreenEntityDesigntimeDefinition extends SimpleScreenEntityDefinition implements ScreenEntityDesigntimeDefinition, Serializable {

	private static final String RECORD = "Record";

	public SimpleScreenEntityDesigntimeDefinition() {
		super(null, null);
	}

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;

	private String packageName;

	private TerminalRectangle snapshotBorders = null;

	private List<SimpleScreenTableDefinition> temporaryTableDefinitions = new ArrayList<SimpleScreenTableDefinition>();

	private List<String> imports = new ArrayList<String>();

	private TerminalSnapshot originalSnapshot;

	private TerminalSnapshot outgoingSnapshot;

	private Collection<TerminalSnapshot> allSnapshots;

	@Override
	public void setEntityName(String entityName) {
		super.setEntityName(entityName);
		populateTableNames(entityName);
	}

	/**
	 * Get names to table. Table rely on the screen entity name, and one screen entity name is set, also set names for tables
	 */
	private void populateTableNames(String entityName) {
		if (temporaryTableDefinitions.size() > 0) {
			int count = 0;
			for (SimpleScreenTableDefinition tableDefinition : temporaryTableDefinitions) {
				setNewTableName(entityName, count, tableDefinition);
				count++;
			}
			temporaryTableDefinitions.clear();
		} else {
			if (entityName.equals(getEntityName())) {
				return;
			}
			Set<String> oldTableNames = getTableDefinitions().keySet();
			Collection<ScreenTableDefinition> tablesDefintions = Collections.unmodifiableCollection(getTableDefinitions().values());
			int count = 0;
			for (ScreenTableDefinition tableDefinition : tablesDefintions) {
				setNewTableName(entityName, count, (SimpleScreenTableDefinition)tableDefinition);
				count++;
			}
			for (String oldTableName : oldTableNames) {
				getTableDefinitions().remove(oldTableName);
			}
		}
	}

	private void setNewTableName(String entityName, int count, SimpleScreenTableDefinition tableDefinition) {
		String tableSuffix = count == 0 ? "" : String.valueOf(count - 1);
		String tableEntityName = MessageFormat.format("{0}{1}{2}", entityName, RECORD, tableSuffix);
		tableDefinition.setTableEntityName(tableEntityName);
		getTableDefinitions().put(tableEntityName, tableDefinition);
	}

	@Override
	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = (TerminalSnapshot)SerializationUtils.clone(terminalSnapshot);
		this.originalSnapshot = (TerminalSnapshot)SerializationUtils.clone(terminalSnapshot);
	}

	@Override
	public TerminalSnapshot getSnapshot() {
		return terminalSnapshot;
	}

	@Override
	public TerminalSnapshot getOriginalSnapshot() {
		return originalSnapshot;
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
		int height = snapshotBorders.getButtomRightPosition().getRow() - snapshotBorders.getTopLeftPosition().getRow();
		int width = snapshotBorders.getButtomRightPosition().getColumn() - snapshotBorders.getTopLeftPosition().getColumn();
		setScreenSize(new SimpleScreenSize(height, width));
	}

	public void addTemporaryTable(SimpleScreenTableDefinition tableDefinition) {
		temporaryTableDefinitions.add(tableDefinition);
	}

	public List<SimpleScreenTableDefinition> getTemporaryTableDefinitions() {
		return temporaryTableDefinitions;
	}

	@Override
	public String toString() {
		return getSnapshot().toString();
	}

	public List<String> getReferredClasses() {
		return imports;
	}

	public TerminalSnapshot getOutgoingSnapshot() {
		return outgoingSnapshot;
	}

	public void setOutgoingSnapshot(TerminalSnapshot outgoingSnapshot) {
		this.outgoingSnapshot = outgoingSnapshot;
	}

	public Collection<TerminalSnapshot> getAllSnapshots() {
		return allSnapshots;
	}

	public void setAllSnapshots(Collection<TerminalSnapshot> allSnapshots) {
		this.allSnapshots = allSnapshots;
	}
}
