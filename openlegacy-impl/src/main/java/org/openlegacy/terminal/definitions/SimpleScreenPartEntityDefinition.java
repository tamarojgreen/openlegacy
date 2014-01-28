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
package org.openlegacy.terminal.definitions;

import org.openlegacy.definitions.AbstractPartEntityDefinition;
import org.openlegacy.terminal.PositionedPart;
import org.openlegacy.terminal.TerminalPosition;

import java.io.Serializable;
import java.util.Collection;

public class SimpleScreenPartEntityDefinition extends AbstractPartEntityDefinition<ScreenFieldDefinition> implements ScreenPartEntityDefinition, PositionedPart, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalPosition partPosition;

	private int width;

	private String displayName;

	public SimpleScreenPartEntityDefinition(Class<?> partClass) {
		super(partClass);
	}

	public TerminalPosition getPartPosition() {
		return partPosition;
	}

	public void setPartPosition(TerminalPosition partPosition) {
		this.partPosition = partPosition;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getTopRow() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();
		int topRow = 999;
		for (ScreenFieldDefinition screenFieldDefinition : fields) {
			int fieldRow = screenFieldDefinition.getPosition().getRow();
			if (fieldRow < topRow) {
				topRow = fieldRow;
			}
		}
		return topRow;
	}

}
