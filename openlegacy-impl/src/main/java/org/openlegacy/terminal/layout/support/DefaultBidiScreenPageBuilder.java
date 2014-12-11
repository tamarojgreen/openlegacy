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
package org.openlegacy.terminal.layout.support;

import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.util.Collections;
import java.util.List;

public class DefaultBidiScreenPageBuilder extends DefaultScreenPageBuilder {

	@Override
	protected int calculateStartColumn(ScreenFieldDefinition field) {
		int column = field.getPosition().getColumn();
		if (field.getDescriptionFieldDefinition() != null) {
			column = field.getDescriptionFieldDefinition().getPosition().getColumn();
		}
		return column - getAdditionalPartWidth();
	}

	@Override
	protected int calculateEndColumn(ScreenFieldDefinition field) {
		if (field.getLabelPosition() != null) {
			int endFieldColumn = field.getLabelPosition().getColumn() + field.getDisplayName().length();
			return endFieldColumn;
		} else {
			int fieldEndColumn = field.getLength() > 0 ? field.getEndPosition().getColumn() : field.getPosition().getColumn()
					+ getDefaultFieldLength();
			return fieldEndColumn;
		}
	}

	@Override
	protected Integer getFieldLogicalStart(int fieldStartColumn, int fieldEndColumn) {
		return fieldEndColumn;
	}

	@Override
	protected int getTableStartColumn(List<ScreenColumnDefinition> columns) {
		// in bidi table start column is the end of the table
		return super.getTableEndColumn(columns);
	}

	@Override
	protected int getTableEndColumn(List<ScreenColumnDefinition> columns) {
		// in bidi table end column is the start of the table
		return super.getTableStartColumn(columns);
	}

	@Override
	protected void sortFields(List<ScreenFieldDefinition> sortedFields) {
		Collections.sort(sortedFields, new TerminalPositionContainerComparator(true));
		bringMessagesToTop(sortedFields);
	}
}
