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
package org.openlegacy.terminal;

import java.io.Serializable;

/**
 * Defines a field on a terminal screen
 * 
 */
public interface TerminalField extends TerminalPositionContainer, Serializable, Cloneable {

	TerminalPosition getPosition();

	TerminalPosition getEndPosition();

	String getValue();

	void setValue(String value);

	int getLength();

	boolean isEditable();

	boolean isModified();

	boolean isEmpty();

	boolean isHidden();

	boolean isPassword();

	Color getColor();

	Color getBackColor();

	/**
	 * The field matching Java type. Used mostly to generate Java class field code from the field type
	 * 
	 * @return
	 */
	Class<?> getType();

	boolean isBold();

	boolean isReversed();

	TerminalField clone();
}
