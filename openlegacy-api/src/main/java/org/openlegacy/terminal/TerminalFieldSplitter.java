package org.openlegacy.terminal;

import org.openlegacy.terminal.TerminalField;

import java.util.List;

/**
 * A field splitter purpose is to split a field to more than one field in case content/behavior is different Common use cases can
 * be blanks, different colors, etc.
 */
public interface TerminalFieldSplitter {

	/**
	 * Split a terminal field to multiple once. If no reason to split, return null
	 * 
	 * @param terminalField
	 * @return
	 */
	List<TerminalField> split(TerminalField terminalField);
}
