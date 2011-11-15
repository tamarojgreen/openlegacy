package org.openlegacy.terminal.actions;

import org.openlegacy.HostAction;

/**
 * A utility class for exposing common host keys action
 * 
 */
public class SendKeyClasses {

	public static class SimpleHostAction implements HostAction {
	}

	public static class UNDEFINED extends SimpleHostAction {
	}

	public static class ENTER extends SimpleHostAction {
	}

	public static class F1 extends SimpleHostAction {
	}

	public static class F3 extends SimpleHostAction {
	}

	public static class PAGEDOWN extends SimpleHostAction {
	}

	public static class PAGEUP extends SimpleHostAction {
	}
}
