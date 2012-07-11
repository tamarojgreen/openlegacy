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
package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;

public interface ModifiableTerminalField extends TerminalField {

	void setPosition(TerminalPosition terminalPosition);

	void setEndPosition(TerminalPosition terminalPosition);

	void setValue(String value, boolean modified);

	void setLength(int length);

}
