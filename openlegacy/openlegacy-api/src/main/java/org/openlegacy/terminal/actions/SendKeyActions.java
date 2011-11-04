package org.openlegacy.terminal.actions;

import org.openlegacy.HostAction;

/**
 * A utility class for exposing common host keys action
 * 
 */
public class SendKeyActions {

	public static final HostAction ENTER = new SendKeyClasses.ENTER();

	public static final HostAction F1 = new SendKeyClasses.F1();
	public static final HostAction F3 = new SendKeyClasses.F3();

	public static final HostAction PAGEUP = new SendKeyClasses.PAGEUP();
	public static final HostAction PAGEDN = new SendKeyClasses.PAGEDOWN();

}
