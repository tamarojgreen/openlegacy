package org.openlegacy.terminal.actions;

import org.openlegacy.HostAction;

/**
 * A utility class for exposing common host keys action
 * 
 */
public class TerminalActions {

	private static interface TerminalAction extends HostAction {
	}

	private static class SimpleTerminalAction implements TerminalAction {
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
