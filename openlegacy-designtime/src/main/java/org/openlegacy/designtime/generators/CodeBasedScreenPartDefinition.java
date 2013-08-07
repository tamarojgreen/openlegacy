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
package org.openlegacy.designtime.generators;

import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.ScreenCodeBasedDefinitionUtils;
import org.openlegacy.terminal.PositionedPart;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;
import org.openlegacy.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CodeBasedScreenPartDefinition extends AbstractCodeBasedPartDefinition<ScreenFieldDefinition, ScreenPojoCodeModel> implements ScreenPartEntityDefinition, PositionedPart {

	private Map<String, ScreenFieldDefinition> fields;

	private int width;
	private TerminalPosition partPosition;

	public CodeBasedScreenPartDefinition(ScreenPojoCodeModel codeModel) {
		super(codeModel);
	}

	@Override
	public Map<String, ScreenFieldDefinition> getFieldsDefinitions() {
		if (fields == null) {
			String fieldName = StringUtil.toJavaFieldName(getCodeModel().getEntityName());
			fields = ScreenCodeBasedDefinitionUtils.getFieldsFromCodeModel(getCodeModel(), fieldName);
		}
		return fields;
	}

	public List<ScreenFieldDefinition> getSortedFields() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
		return sortedFields;
	}

	@Override
	public String getPartName() {
		return getCodeModel().getEntityName();
	}

	public TerminalPosition getPartPosition() {
		if (partPosition == null) {
			partPosition = getCodeModel().getPartPosition();
		}
		return partPosition;
	}

	public int getWidth() {
		if (width == 0) {
			width = getCodeModel().getPartWidth();
		}
		return width;
	}

	@Override
	public String getDisplayName() {
		return getCodeModel().getDisplayName();
	}

	public void setWidth(int width) {
		this.width = width;

	}

	public void setPartPosition(TerminalPosition partPosition) {
		this.partPosition = partPosition;
	}

	public boolean isSupportTerminalData() {
		return getCodeModel().isSupportTerminalData();
	}

	@Override
	public String getClassName() {
		return getCodeModel().getClassName();
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
