package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.List;

public interface ScreenEntityDefinitionsBuilder {

	/**
	 * Select the best match field for screen name from the list of fields which matches the title pattern
	 */
	void selectPotentialScreenEntityName(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields);

	ScreenFieldDefinition addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField editableField, TerminalField labelField);

	void processFact(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact);
}
