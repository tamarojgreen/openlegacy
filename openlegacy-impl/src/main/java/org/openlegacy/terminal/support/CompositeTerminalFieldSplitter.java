package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldSplitter;

import java.util.List;

public class CompositeTerminalFieldSplitter implements TerminalFieldSplitter {

	private List<TerminalFieldSplitter> terminalFieldSplitters;

	public List<TerminalField> split(TerminalField terminalField) {
		for (TerminalFieldSplitter terminalFieldSplitter : terminalFieldSplitters) {
			List<TerminalField> splittedFields = terminalFieldSplitter.split(terminalField);
			if (splittedFields != null) {
				return splittedFields;
			}
		}
		return null;
	}

	public void setTerminalFieldSplitters(List<TerminalFieldSplitter> terminalFieldSplitters) {
		this.terminalFieldSplitters = terminalFieldSplitters;
	}
}
