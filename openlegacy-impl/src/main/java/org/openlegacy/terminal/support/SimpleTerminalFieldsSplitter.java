package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldSplitter;
import org.openlegacy.terminal.TerminalFieldsSplitter;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SimpleTerminalFieldsSplitter implements TerminalFieldsSplitter {

	@Inject
	TerminalFieldSplitter terminalFieldSplitter;

	public List<TerminalField> splitFields(List<TerminalField> fields) {
		List<TerminalField> logicalFields = new ArrayList<TerminalField>();
		for (TerminalField terminalField : fields) {
			List<TerminalField> splittedFields = terminalFieldSplitter.split(terminalField);
			if (splittedFields != null) {
				logicalFields.addAll(splittedFields);
			} else {
				logicalFields.add(terminalField);
			}

		}
		return logicalFields;
	}

	public List<TerminalField> splitFields(TerminalSnapshot terminalSnapshot) {
		return splitFields(terminalSnapshot.getFields());
	}

}
