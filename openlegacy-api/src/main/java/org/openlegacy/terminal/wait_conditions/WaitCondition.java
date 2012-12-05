package org.openlegacy.terminal.wait_conditions;

import org.openlegacy.terminal.TerminalSession;

/**
 * A wait condition defines whether to wait on a given state on the terminal session Also defines the total timeout to wait, and
 * in what interval to check. Defaults wait interval and wait timeout are determined by waitConditionFactory bean
 * 
 * @author Roi Mor
 * 
 */
public interface WaitCondition {

	boolean continueWait(TerminalSession terminalSession);

	long getWaitInterval();

	long getWaitTimeout();

}
