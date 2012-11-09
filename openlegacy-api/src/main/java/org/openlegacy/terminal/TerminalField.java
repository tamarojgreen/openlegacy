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

	/**
	 * The position (row, column) of the field
	 * 
	 * @return position of the field
	 */
	TerminalPosition getPosition();

	/**
	 * The end position (row, column) of the field
	 * 
	 * @return end position of the field
	 */
	TerminalPosition getEndPosition();

	/**
	 * The value of the field
	 * 
	 * @return value of the field
	 */
	String getValue();

	/**
	 * The original value of the field before set by user
	 * 
	 * @return original value of the field
	 */
	String getOriginalValue();

	/**
	 * Sets the value of the field for editable fields
	 * 
	 * @param value
	 *            the new value
	 */
	void setValue(String value);

	/**
	 * The length of the field. Typically determined by distance between end position and position
	 * 
	 * @return the length of the field
	 */
	int getLength();

	/**
	 * Is the field editable on the host screen (protected)
	 * 
	 * @return is the field editable
	 */
	boolean isEditable();

	/**
	 * Is the field modified. Used to determine whether to send the value to the host
	 * 
	 * @return is the field modified
	 */
	boolean isModified();

	/**
	 * Is the field empty (null of empty string)
	 * 
	 * @return Is the field empty
	 */
	boolean isEmpty();

	/**
	 * Is the field is hidden. A password is a hidden editable (protected) field
	 * 
	 * @return is the field hidden on the host screen
	 */
	boolean isHidden();

	/**
	 * Is the field password. A password is always an editable field
	 * 
	 * @return is the field password
	 */
	boolean isPassword();

	/**
	 * Gets the field color
	 * 
	 * @return the field color
	 */
	Color getColor();

	/**
	 * Gets the field background color
	 * 
	 * @return the field background color
	 */
	Color getBackColor();

	/**
	 * The field matching Java type. Used mostly to generate Java class field code from the field type
	 * 
	 * @return the field Java type
	 */
	Class<?> getType();

	/**
	 * Whether the field is bold (intensified) on the host screen
	 * 
	 * @return is the field is bold
	 */
	boolean isBold();

	/**
	 * Whether the field is reversed (has background color) on the host screen
	 * 
	 * @return is the field is reversed
	 */
	boolean isReversed();

	/**
	 * Holds the fields visual
	 * 
	 * @return visual value from the host
	 */
	String getVisualValue();

	/**
	 * Get access to the underlying implementation
	 * 
	 * @return the underlying implementation
	 */
	Object getDelegate();

	/**
	 * Weather the field is displayed from right to left
	 * 
	 * @return is the field is displayed from right to left
	 */
	boolean isRightToLeft();

	/**
	 * Clones the field
	 * 
	 * @return a cloned copy of the field
	 */
	TerminalField clone();

}
