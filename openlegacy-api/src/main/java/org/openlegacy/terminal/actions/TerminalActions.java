/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.actions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.exceptions.TerminalActionException;
import org.openlegacy.terminal.exceptions.TerminalActionNotMappedException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;

/**
 * A utility class which contains all common terminal actions. Contains both class for usages by annotation definition, and
 * initialized instances for direct usage from API.
 * 
 * @author Roi Mor
 * 
 */
public class TerminalActions {

	public static class SimpleTerminalMappedAction implements TerminalMappedAction, Serializable {

		private static final long serialVersionUID = 1L;

		public void perform(TerminalSession terminalSession, Object entity, Object... keys) {
			// if we got here it means the actions is not mapped...
			throw (new TerminalActionNotMappedException(MessageFormat.format(
					"Specified action {0} is not mapped to a terminal command", getClass())));
		}

		public String getActionName() {
			return getClass().getSimpleName();
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

	public static class NULL extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static class NONE extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static NONE NONE() {
		return new NONE();
	}

	public static class ENTER extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static ENTER ENTER() {
		return new ENTER();
	}

	public static class ESC extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static ESC ESC() {
		return new ESC();
	}

	public static class F1 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F1 F1() {
		return new F1();
	}

	public static class F2 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F2 F2() {
		return new F2();
	}

	public static class F3 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F3 F3() {
		return new F3();
	}

	public static class F4 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F4 F4() {
		return new F4();
	}

	public static class F5 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F5 F5() {
		return new F5();
	}

	public static class F6 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F6 F6() {
		return new F6();
	}

	public static class F7 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F7 F7() {
		return new F7();
	}

	public static class F8 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F8 F8() {
		return new F8();
	}

	public static class F9 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F9 F9() {
		return new F9();
	}

	public static class F10 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F10 F10() {
		return new F10();
	}

	public static class F11 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F11 F11() {
		return new F11();
	}

	public static class F12 extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static F12 F12() {
		return new F12();
	}

	public static class PAGEDOWN extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static PAGEDOWN PAGEDOWN() {
		return new PAGEDOWN();
	}

	public static class PAGEUP extends SimpleTerminalMappedAction {

		private static final long serialVersionUID = 1L;
	}

	public static PAGEUP PAGEUP() {
		return new PAGEUP();
	}

	/**
	 * Retrieve a command value which is mapped to the given keyboradKey Used for parsing JavaScript keyboard actions, sent via
	 * http request
	 * 
	 * @param keyboardKey
	 * @param terminalActionMapper
	 * @return A command which to the given keyboardKey
	 * @throws TerminalActionNotMappedException
	 */
	@SuppressWarnings("unchecked")
	public static Object getCommand(String keyboardKey, TerminalActionMapper terminalActionMapper)
			throws TerminalActionNotMappedException {
		String[] keyboardActionParts = keyboardKey.split("-");

		Class<? extends TerminalAction> keyboardClazz;
		AdditionalKey additionalKey = AdditionalKey.NONE;
		TerminalAction terminalAction = null;
		Object command;
		try {
			if (keyboardActionParts.length == 2) {
				keyboardKey = keyboardActionParts[1];
				String keyboardKeyClass = MessageFormat.format("{0}${1}", TerminalActions.class.getName(), keyboardKey);
				keyboardClazz = (Class<? extends TerminalAction>)Class.forName(keyboardKeyClass);
				additionalKey = AdditionalKey.valueOf(keyboardActionParts[0]);
				terminalAction = (TerminalAction)TerminalActions.combined(additionalKey, keyboardClazz);
			} else {
				String keyboardKeyClass = MessageFormat.format("{0}${1}", TerminalActions.class.getName(), keyboardKey);
				keyboardClazz = (Class<? extends TerminalAction>)Class.forName(keyboardKeyClass);
				terminalAction = keyboardClazz.newInstance();
			}
			command = terminalActionMapper.getCommand(terminalAction);
		} catch (Exception e) {
			throw (new TerminalActionNotMappedException(MessageFormat.format(
					"The keyboard key {0} has not been mapped to any command", keyboardKey), e));
		}
		return command;

	}

	public static TerminalAction newAction(String actionName) {
		Class<?>[] classes = TerminalActions.class.getClasses();
		TerminalAction action = null;
		for (Class<?> clazz : classes) {
			Constructor<?>[] ctors = clazz.getDeclaredConstructors();
			for (Constructor<?> ctor : ctors) {
				if (actionName.equals(clazz.getSimpleName())) {
					ctor.setAccessible(true);
					try {
						action = (TerminalAction)ctor.newInstance();
					} catch (Exception e) {
						throw new TerminalActionException(MessageFormat.format("Cannot instantiate an action with name {0}",
								actionName));
					}
					break;
				}
			}
			if (action != null) {
				break;
			}
		}
		return action;
	}
}
