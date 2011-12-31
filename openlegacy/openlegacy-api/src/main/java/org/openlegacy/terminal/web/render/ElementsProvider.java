package org.openlegacy.terminal.web.render;

import org.openlegacy.terminal.TerminalField;
import org.w3c.dom.Node;

public interface ElementsProvider {

	void createInput(Node rootNode, TerminalField terminalField);

	void createLabel(Node rootNode, TerminalField terminalField);

}
