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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

public class DateFieldFact implements ScreenFact {

	private TerminalField labelField;
	private TerminalField leftTerminalField;
	private ScreenFieldDefinition leftField;
	private ScreenFieldDefinition middleField;
	private ScreenFieldDefinition rightField;

	// used by single date field format only
	private String datePattern;

	/**
	 * Constructor for single date field
	 * 
	 * @param labelField
	 * @param leftTerminalField
	 * @param leftField
	 */
	public DateFieldFact(TerminalField labelField, TerminalField leftTerminalField, ScreenFieldDefinition leftField,
			String datePattern) {
		this.labelField = labelField;
		this.leftTerminalField = leftTerminalField;
		this.leftField = leftField;
		this.datePattern = datePattern;
	}

	/**
	 * Constructor for 3 date fields
	 * 
	 * @param labelField
	 * @param leftTerminalField
	 * @param leftField
	 * @param middleField
	 * @param rightField
	 */
	public DateFieldFact(TerminalField labelField, TerminalField leftTerminalField, ScreenFieldDefinition leftField,
			ScreenFieldDefinition middleField, ScreenFieldDefinition rightField) {
		this.labelField = labelField;
		this.leftTerminalField = leftTerminalField;
		this.leftField = leftField;
		this.middleField = middleField;
		this.rightField = rightField;
	}

	public TerminalField getLabelField() {
		return labelField;
	}

	public TerminalField getLeftTerminalField() {
		return leftTerminalField;
	}

	public ScreenFieldDefinition getLeftField() {
		return leftField;
	}

	public ScreenFieldDefinition getMiddleField() {
		return middleField;
	}

	public ScreenFieldDefinition getRightField() {
		return rightField;
	}

	public String getDatePattern() {
		return datePattern;
	}
}
