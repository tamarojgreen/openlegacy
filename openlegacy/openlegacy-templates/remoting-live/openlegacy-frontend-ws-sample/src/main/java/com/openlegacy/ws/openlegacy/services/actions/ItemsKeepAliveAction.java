package com.openlegacy.ws.openlegacy.services.actions;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;

import java.io.Serializable;

public class ItemsKeepAliveAction implements TerminalAction, Serializable {

	private static final long serialVersionUID = 1L;

	public void perform(TerminalSession terminalSession, Object entity, Object... keys) {
		// PLACE HOLDER for keep alive action
	}

	@Override
	public boolean isMacro() {
		// TODO Auto-generated method stub
		return false;
	}
}