package org.openlegacy.terminal;

import java.util.List;

public interface TerminalFieldsSplitter {

	List<TerminalField> splitFields(TerminalSnapshot terminalSnapshot);

	List<TerminalField> splitFields(List<TerminalField> fields);
}
