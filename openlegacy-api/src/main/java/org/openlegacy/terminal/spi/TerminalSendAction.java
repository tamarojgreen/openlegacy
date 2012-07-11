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
package org.openlegacy.terminal.spi;

import org.openlegacy.SendAction;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public interface TerminalSendAction extends SendAction {

	List<TerminalField> getModifiedFields();

	Object getCommand();

	TerminalPosition getCursorPosition();

	void setCursorPosition(TerminalPosition fieldPosition);
}