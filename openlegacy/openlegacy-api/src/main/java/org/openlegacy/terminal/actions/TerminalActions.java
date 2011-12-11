package org.openlegacy.terminal.actions;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.TerminalActionNotMappedException;

import java.text.MessageFormat;

/**
 * A utility class for exposing common terminal actions
 * 
 */
public class TerminalActions {

	private static class TerminalMappedAction implements TerminalAction {

		public void perform(TerminalSession terminalSession, Object entity) {
			// if we got here it means the actions is not mapped...
			throw (new TerminalActionNotMappedException(MessageFormat.format(
					"Specified action {0} is not mapped to a terminal command", getClass())));
		}
	}

	public static class ENTER extends TerminalMappedAction {
	}

	public static ENTER ENTER() {
		return new ENTER();
	}

	public static class ESC extends TerminalMappedAction {
	}

	public static ESC ESC() {
		return new ESC();
	}

	public static class F1 extends TerminalMappedAction {
	}

	public static F1 F1() {
		return new F1();
	}

	public static class F2 extends TerminalMappedAction {
	}

	public static F2 F2() {
		return new F2();
	}

	public static class F3 extends TerminalMappedAction {
	}

	public static F3 F3() {
		return new F3();
	}

	public static class F4 extends TerminalMappedAction {
	}

	public static F4 F4() {
		return new F4();
	}

	public static class F5 extends TerminalMappedAction {
	}

	public static F5 F5() {
		return new F5();
	}

	public static class F6 extends TerminalMappedAction {
	}

	public static F6 F6() {
		return new F6();
	}

	public static class F7 extends TerminalMappedAction {
	}

	public static F7 F7() {
		return new F7();
	}

	public static class F8 extends TerminalMappedAction {
	}

	public static F8 F8() {
		return new F8();
	}

	public static class F9 extends TerminalMappedAction {
	}

	public static F9 F9() {
		return new F9();
	}

	public static class F10 extends TerminalMappedAction {
	}

	public static F10 F10() {
		return new F10();
	}

	public static class F11 extends TerminalMappedAction {
	}

	public static F11 F11() {
		return new F11();
	}

	public static class F12 extends TerminalMappedAction {
	}

	public static F12 F12() {
		return new F12();
	}

	public static class PAGEDOWN extends TerminalMappedAction {
	}

	public static PAGEDOWN PAGEDOWN() {
		return new PAGEDOWN();
	}

	public static class PAGEUP extends TerminalMappedAction {
	}

	public static PAGEUP PAGEUP() {
		return new PAGEUP();
	}
}
