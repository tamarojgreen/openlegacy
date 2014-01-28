/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
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
