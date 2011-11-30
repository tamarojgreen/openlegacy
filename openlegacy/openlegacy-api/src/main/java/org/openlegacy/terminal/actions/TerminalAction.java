package org.openlegacy.terminal.actions;

import org.openlegacy.SessionAction;
import org.openlegacy.terminal.TerminalSession;

/**
 * A terminal action is an action performed on a <code>TerminalSession</code> Can be either a mapped action (
 * <code>TerminalActions.ENTER(), etc</code>) or a custom action which implements this interface and perform a sequence of mapped
 * actions (macro style)
 * 
 */
public interface TerminalAction extends SessionAction<TerminalSession> {
}
