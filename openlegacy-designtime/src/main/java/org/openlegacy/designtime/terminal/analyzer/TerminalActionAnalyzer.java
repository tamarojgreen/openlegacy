package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

/**
 * Translate an action + caption to a well defined action definition model
 * 
 */
public interface TerminalActionAnalyzer {

	TerminalActionDefinition toTerminalActionDefinition(String action, String caption, TerminalPosition position);
}
