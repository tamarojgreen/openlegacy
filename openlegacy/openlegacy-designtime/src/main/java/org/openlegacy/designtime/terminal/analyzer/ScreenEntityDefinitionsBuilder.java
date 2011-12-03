package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.terminal.model.TableColumn;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.List;

public interface ScreenEntityDefinitionsBuilder {

	void addIdentifier(SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field);

	void setScreenEntityName(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field);

	void addField(ScreenEntityDefinition screenEntityDefinition, TerminalField editableField, String leadingLabel);

	TableColumn addTableColumn(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields);

	void addTableDefinition(ScreenEntityDefinition screenEntityDefinition, List<TableColumn> tableColumns);

	void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, String text, ScreenPosition position, String regex);
}
