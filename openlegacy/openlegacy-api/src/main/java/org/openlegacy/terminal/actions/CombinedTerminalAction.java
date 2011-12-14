package org.openlegacy.terminal.actions;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.TerminalActionNotMappedException;

import java.text.MessageFormat;

public class CombinedTerminalAction implements TerminalAction {

	private AdditionalKey additionalKey;
	private Class<? extends TerminalAction> terminalAction;

	public void perform(TerminalSession session, Object entity) {
		// if we got here it means the actions is not mapped...
		throw (new TerminalActionNotMappedException(MessageFormat.format(
				"Specified action {0} is not mapped to a terminal command", getClass())));
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}

	public Class<? extends TerminalAction> getTerminalAction() {
		return terminalAction;
	}

	public void setTerminalAction(Class<? extends TerminalAction> terminalAction) {
		this.terminalAction = terminalAction;
	}

	@Override
	public int hashCode() {
		return getAdditionalKey().hashCode() + getTerminalAction().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CombinedTerminalAction)) {
			return false;
		}
		CombinedTerminalAction other = (CombinedTerminalAction)obj;
		return other.getAdditionalKey().equals(additionalKey) & other.getTerminalAction().equals(getTerminalAction());
	}
}
