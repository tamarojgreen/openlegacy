package org.openlegacy.terminal.web.render;

import org.openlegacy.terminal.TerminalField;
import org.w3c.dom.Document;

public interface ElementsProvider<T, R> {

	T createInput(R rootNode, TerminalField terminalField);

	T createHidden(R rootNode, String name);

	T createLabel(R rootNode, TerminalField terminalField);

	T createWrapperTag(Document doc);

	T createStyleTag(R rootNode, String styleSettings);

	T createFormTag(R rootNode, String formActionURL, String formMethod, String formName);
}
