package org.openlegacy.designtime.terminal.model;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;

import java.io.Serializable;

public class SimpleScreenEntityDesigntimeDefinition extends SimpleScreenEntityDefinition implements ScreenEntityDesigntimeDefinition, Serializable {

	public SimpleScreenEntityDesigntimeDefinition() {
		super(null, null);
	}

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;

	private String packageName;

	@Override
	public void setEntityName(String entityName) {
		super.setEntityName(entityName);
	}

	@Override
	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
	}

	@Override
	public TerminalSnapshot getSnapshot() {
		return terminalSnapshot;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
