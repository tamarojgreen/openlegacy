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

import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length. Typically loaded into
 * {@link ScreenEntityDefinition}, and loaded from {@link ScreenField} annotation
 * 
 * @author Roi Mor
 */
public interface ScreenFieldDefinition extends FieldDefinition, TerminalPositionContainer, Comparable<ScreenFieldDefinition> {

	/**
	 * Gets the field position (row, column)
	 */
	TerminalPosition getPosition();

	/**
	 * Gets the field end position (row, column)
	 */
	TerminalPosition getEndPosition();

	/**
	 * Gets the field leading label position (row, column)
	 */
	TerminalPosition getLabelPosition();

	/**
	 * Gets the field length, Typically calculated from end position and position (start)
	 */
	int getLength();

	/**
	 * Is the field editable on the host screen (protected)
	 * 
	 * @return is the field editable
	 */
	boolean isEditable();

	/**
	 * Is the field password. A password is always an editable field
	 * 
	 * @return is the field password
	 */
	boolean isPassword();

	/**
	 * Used mostly for design-time usage
	 * 
	 * @return a sample terminal field
	 */
	TerminalField getTerminalField();

	/**
	 * Used mostly for design-time usage
	 * 
	 * @return a sample leading label terminal field
	 */
	TerminalField getTerminalLabelField();

	/**
	 * When the endRow doesn't match the start row, determine whether to grab a rectangle or as breaking lines
	 * 
	 * @return
	 */
	boolean isRectangle();

	boolean isRightToLeft();

	FieldAttributeType getAttribute();

	/**
	 * Used mostly for design-time usage
	 * 
	 * @return a regular expression used for filtering
	 */
	String getWhenFilter();

	/**
	 * Used mostly for design-time usage
	 * 
	 * @return a regular expression used for negative filtering (unless)
	 */
	String getUnlessFilter();
}
