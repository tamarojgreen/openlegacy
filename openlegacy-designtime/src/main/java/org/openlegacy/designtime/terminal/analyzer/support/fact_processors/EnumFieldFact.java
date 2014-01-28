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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

public class EnumFieldFact implements ScreenFact {

	private ScreenFieldDefinition enumFieldDefinition;
	private String entrySeperators;
	private String pairSeperators;
	private TerminalField enumField;

	public EnumFieldFact(ScreenFieldDefinition enumFieldDefinition, TerminalField enumField, String entrySeperators,
			String pairSeperators) {
		this.enumFieldDefinition = enumFieldDefinition;
		this.enumField = enumField;
		this.entrySeperators = entrySeperators;
		this.pairSeperators = pairSeperators;
	}

	public String getEntrySeperators() {
		return entrySeperators;
	}

	public ScreenFieldDefinition getEnumFieldDefinition() {
		return enumFieldDefinition;
	}

	public TerminalField getEnumField() {
		return enumField;
	}

	public String getPairSeperators() {
		return pairSeperators;
	}
}
