package org.openlegacy.adapter.terminal;

import org.openlegacy.HostAction;

/**
 * A utility class for exposing common host keys action
 * 
 */
public class SendKeyClasses {

	public static class HostActionAdapter implements HostAction {

		public Object getCommand() {
			return getClass().getSimpleName();
		}

	}

	public static class ENTER extends HostActionAdapter {
	}

	public static class PF1 extends HostActionAdapter {
	}

	public static class PAGEDN extends HostActionAdapter {
	}

	public static class PAGEUP extends HostActionAdapter {
	}
}
