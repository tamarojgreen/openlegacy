package org.openlegacy.terminal.actions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.exceptions.TerminalActionNotMappedException;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * A utility class for exposing common terminal actions
 * 
 */
public class TerminalActions {

	private static class TerminalMappedAction implements TerminalAction, Serializable {

		private static final long serialVersionUID = 1L;

		public void perform(TerminalSession terminalSession, Object entity) {
			// if we got here it means the actions is not mapped...
			throw (new TerminalActionNotMappedException(MessageFormat.format(
					"Specified action {0} is not mapped to a terminal command", getClass())));
		}

		@Override
		public int hashCode() {
			return getClass().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj.getClass().equals(getClass());
		}
	}

	public static SessionAction<? extends Session> combined(AdditionalKey additionalKey, TerminalAction terminalAction) {
		return combined(additionalKey, terminalAction.getClass());
	}

	public static SessionAction<? extends Session> combined(AdditionalKey additionalKey,
			Class<? extends TerminalAction> terminalActionClass) {
		CombinedTerminalAction combinedAction = new CombinedTerminalAction();
		combinedAction.setAdditionalKey(additionalKey);
		combinedAction.setTerminalAction(terminalActionClass);
		return combinedAction;
	}

	public static class ENTER extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static ENTER ENTER() {
		return new ENTER();
	}

	public static class ESC extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static ESC ESC() {
		return new ESC();
	}

	public static class F1 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F1 F1() {
		return new F1();
	}

	public static class F2 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F2 F2() {
		return new F2();
	}

	public static class F3 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F3 F3() {
		return new F3();
	}

	public static class F4 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F4 F4() {
		return new F4();
	}

	public static class F5 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F5 F5() {
		return new F5();
	}

	public static class F6 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F6 F6() {
		return new F6();
	}

	public static class F7 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F7 F7() {
		return new F7();
	}

	public static class F8 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F8 F8() {
		return new F8();
	}

	public static class F9 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F9 F9() {
		return new F9();
	}

	public static class F10 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F10 F10() {
		return new F10();
	}

	public static class F11 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F11 F11() {
		return new F11();
	}

	public static class F12 extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F12 F12() {
		return new F12();
	}

	public static class PAGEDOWN extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static PAGEDOWN PAGEDOWN() {
		return new PAGEDOWN();
	}

	public static class PAGEUP extends TerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static PAGEUP PAGEUP() {
		return new PAGEUP();
	}

	public static Object getCommand(String keyboardKey, TerminalActionMapper terminalActionMapper)
			throws TerminalActionNotMappedException {
		String keyboardKeyClass = MessageFormat.format("{0}${1}", TerminalActions.class.getName(), keyboardKey);
		Class<?> keyboardClazz;
		Object command;
		try {
			keyboardClazz = Class.forName(keyboardKeyClass);
			command = terminalActionMapper.getCommand((TerminalAction)keyboardClazz.newInstance());
		} catch (Exception e) {
			throw (new TerminalActionNotMappedException(MessageFormat.format(
					"The keyboard key {0} has not been mapped to any command", keyboardKey), e));
		}
		return command;

	}
}
