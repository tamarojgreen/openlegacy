package org.openlegacy.providers.tn5250j;

import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.tn5250j.Session5250;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenOIA;

import java.util.List;

public class Tn5250jTerminalConnection implements TerminalConnection {

	private Session5250 session;

	private int waitForUnlock = 300;

	// adding sequence support to tn5250j which doesn't support it
	private int sequence = 1;

	public Tn5250jTerminalConnection(Session5250 session) {
		this.session = session;
	}

	public TerminalSnapshot getSnapshot() {
		return new Tn5250jTerminalSnapshot(session.getScreen(), sequence);
	}

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {
		sequence++;
		TerminalSnapshot snapshot = getSnapshot();
		List<TerminalField> modifiedFields = terminalSendAction.getModifiedFields();
		for (TerminalField terminalField : modifiedFields) {
			TerminalField field = snapshot.getField(terminalField.getPosition());
			field.setValue(terminalField.getValue());
		}
		Screen5250 screen = session.getScreen();
		TerminalPosition cursorPosition = terminalSendAction.getCursorPosition();
		if (cursorPosition != null) {
			screen.setCursor(cursorPosition.getColumn(), cursorPosition.getRow());
		}

		waitForKeyboardUnlock((String)terminalSendAction.getCommand());
		sequence++;
		return this;
	}

	private void waitForKeyboardUnlock(String aid) {
		Screen5250 screen = session.getScreen();
		screen.sendKeys(aid);
		while (screen.getOIA().getInputInhibited() == ScreenOIA.INPUTINHIBITED_SYSTEM_WAIT
				&& screen.getOIA().getLevel() != ScreenOIA.OIA_LEVEL_INPUT_ERROR) {
			try {
				Thread.sleep(waitForUnlock);
			} catch (InterruptedException ex) {
				// do nothing
			}
		}

	}

	public Object getDelegate() {
		return session;
	}

	public void setWaitForUnlock(int waitForUnlock) {
		this.waitForUnlock = waitForUnlock;
	}

	public TerminalSnapshot fetchSnapshot() {
		return getSnapshot();
	}

	public String getSessionId() {
		return String.valueOf(session.getSessionName());
	}

	public boolean isConnected() {
		return session.isConnected();
	}

	public void disconnect() {
		session.disconnect();
	}
}
