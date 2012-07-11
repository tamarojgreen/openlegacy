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
package org.openlegacy.terminal.definitions;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public interface ScreenFieldDefinition extends FieldDefinition, TerminalPositionContainer, Comparable<ScreenFieldDefinition> {

	TerminalPosition getPosition();

	TerminalPosition getEndPosition();

	TerminalPosition getLabelPosition();

	int getLength();

	boolean isEditable();

	boolean isPassword();

	Class<?> getJavaType();

	/**
	 * Used mostly for designtime usage
	 * 
	 * @return
	 */
	TerminalField getTerminalField();

	/**
	 * Used mostly for designtime usage
	 * 
	 * @return
	 */
	TerminalField getTerminalLabelField();
}
