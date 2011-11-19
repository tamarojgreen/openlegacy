package org.openlegacy.terminal.actions;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.HostActionNotMappedException;

import java.text.MessageFormat;

/**
 * A utility class for exposing common host keys action
 * 
 */
public class TerminalActions {

	private static class SimpleTerminalAction implements TerminalAction {

		public void perform(TerminalSession hostSession, Object entity) {
			throw (new HostActionNotMappedException(MessageFormat.format(
					"Specified action {0} is not mapped to a terminal command", getClass())));
		}
	}

	public static class ENTER extends SimpleTerminalAction {
	}

	public static ENTER ENTER() {
		return new ENTER();
	}

	public static class F1 extends SimpleTerminalAction {
	}

	public static F1 F1() {
		return new F1();
	}

	public static class F3 extends SimpleTerminalAction {
	}

	public static F3 F3() {
		return new F3();
	}

	public static class PAGEDOWN extends SimpleTerminalAction {
	}

	public static PAGEDOWN PAGEDOWN() {
		return new PAGEDOWN();
	}

	public static class PAGEUP extends SimpleTerminalAction {
	}

	public static PAGEUP PAGEUP() {
		return new PAGEUP();
	}
}
