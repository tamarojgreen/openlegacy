package org.openlegacy.designtime.terminal.model;

import org.openlegacy.EntityType;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenTableDefinition;

import java.util.List;

public interface ScreenEntityDesigntimeDefinition extends ScreenEntityDefinition {

	void setEntityName(String entityName);

	void setType(Class<? extends EntityType> entityType);

	void setDisplayName(String displayName);

	TerminalSnapshot getSnapshot();

	void setSnapshot(TerminalSnapshot terminalSnapshot);

	String getPackageName();

	void setPackageName(String packageName);

	TerminalRectangle getSnapshotBorders();

	void setSnapshotBorders(TerminalRectangle snapshotBorders);

	/**
	 * Adds a table to the screen definition without a name yet. Name will be given by the screen entity name at a pater phase
	 */
	void addTemporaryTable(SimpleScreenTableDefinition tableDefinition);

	List<SimpleScreenTableDefinition> getTemporaryTableDefinitions();

	List<ScreenFieldDefinition> getSortedFields();

	List<String> getReferredClasses();

	void setNavigationDefinition(NavigationDefinition navigationDefinition);

	void setAccessedFromSnapshot(TerminalSnapshot accessedFromSnapshot);

	void setAccessedFromScreenDefinition(ScreenEntityDefinition screenEntityDefinition);
}