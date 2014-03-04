package org.openlegacy.modules.menu;

import org.openlegacy.terminal.TerminalSnapshot;

public interface MenuOptionFinder {

	String findMenuOption(TerminalSnapshot terminalSnapshot, String text);
}
