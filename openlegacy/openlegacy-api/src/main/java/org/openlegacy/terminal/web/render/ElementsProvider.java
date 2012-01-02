package org.openlegacy.terminal.web.render;

import org.openlegacy.terminal.TerminalField;

public interface ElementsProvider<T> {

	T createInput(T rootTag, TerminalField terminalField);

	T createHidden(T rootTag, String name);

	T createLabel(T rootTag, TerminalField terminalField);

	T createStyleTag(T rootTag, String styleSettings);

	T createFormTag(T rootTag, String formActionURL, String formMethod, String formName);
}
